package com.chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.blackhole.util.LogUtil;
import com.chengweiou.universe.carina.base.converter.Account;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.person.Person;
import com.chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import com.chengweiou.universe.carina.model.entity.room.Room;
import com.chengweiou.universe.carina.service.person.PersonService;
import com.chengweiou.universe.carina.service.room.PersonRoomRelateService;
import com.chengweiou.universe.carina.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController("meRoomController")
@RequestMapping("me")
public class RoomController {
    @Autowired
    private RoomService service;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRoomRelateService relateService;

    @GetMapping("/room")
    public Rest<Long> enterRoom(Room e, PersonRoomRelate personRoomRelate, @RequestHeader("loginAccount") Account loginAccount)  throws ParamException, FailException, ProjException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("room.id | personIdList", e.getId(), e.getPersonIdList()).are().notAllNull();
        // todo test
        if (e.getId() != null) {
            Valid.check("room.id ", e.getId()).is().positive();
            Room indb = service.findById(e);
            return Rest.ok(indb);
        }

        if (e.getPersonIdList().size() > 1) Valid.check("personRoomRelate.name", personRoomRelate.getName()).is().lengthIn(100);
        e.getPersonIdList().add(loginAccount.getPerson().getId());
        if (e.getPersonIdList().size() == 2) {
            List<Room> indbList = service.find(new SearchCondition(), e);
            indbList = indbList.stream().filter(each -> each.getPersonIdList().size() == 2).collect(Collectors.toList());
            if (indbList.size() == 1) return Rest.ok(indbList.get(0));
            // todo 如果多于一个, 出错, 群组，然后人离开了？
        }
        service.save(e);
        // todo add room type?
        // 保存到relate表
        List<Person> personList = e.getPersonIdList().parallelStream().map(id -> personService.findById(Builder.set("id", id).to(new Person())))
                .filter(person -> person.notNull()).sorted().collect(Collectors.toList());
        List<PersonRoomRelate> relateList = new ArrayList<>();
        if (personList.size() == 2) {
            relateList.add(Builder.set("person", personList.get(0)).set("room", e).set("name", personList.get(1).getName()).set("imgsrc", personList.get(1).getImgsrc()).to(new PersonRoomRelate()));
            relateList.add(Builder.set("person", personList.get(1)).set("room", e).set("name", personList.get(0).getName()).set("imgsrc", personList.get(0).getImgsrc()).to(new PersonRoomRelate()));
        } else {
            relateList.addAll(personList.stream().map(person -> Builder.set("person", person).set("room", e).to(personRoomRelate)).collect(Collectors.toList()));
        }
        relateList.forEach(relate -> {
            try {
                relateService.save(relate);
            } catch (FailException ex) {
                LogUtil.e("relate list: " + relate.toString(), ex);
                ex.printStackTrace();
            }
        });
        // todo send system hello message
        return Rest.ok(e);
    }

    @DeleteMapping("/room/{id}")
    public Rest<Boolean> delete(Room e) throws ParamException, FailException {
        Valid.check("room.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }
}
