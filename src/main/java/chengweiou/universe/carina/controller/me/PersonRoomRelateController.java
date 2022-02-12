package chengweiou.universe.carina.controller.me;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelateStatus;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;

@RestController("mePersonRoomRelateController")
@RequestMapping("me")
public class PersonRoomRelateController {
    @Autowired
    private PersonRoomRelateDio dio;

    @GetMapping("/personRoomRelate/key")
    public Rest<PersonRoomRelate> findByKey(PersonRoomRelate e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("personRoomRelate.room.id", e.getRoom().getId()).is().positive();
        e.setPerson(loginAccount.getPerson());
        PersonRoomRelate list = dio.findByKey(e);
        return Rest.ok(list);
    }

    @GetMapping("/personRoomRelate/count")
    public Rest<Long> count(SearchCondition searchCondition, @RequestHeader("loginAccount")Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        long count = dio.count(searchCondition, Builder.set("person", loginAccount.getPerson()).set("status", PersonRoomRelateStatus.SHOW).to(new PersonRoomRelate()));
        return Rest.ok(count);
    }

    @GetMapping("/personRoomRelate")
    public Rest<List<PersonRoomRelate>> find(SearchCondition searchCondition, @RequestHeader("loginAccount")Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        List<PersonRoomRelate> list = dio.find(searchCondition, Builder.set("person", loginAccount.getPerson()).set("status", PersonRoomRelateStatus.SHOW).to(new PersonRoomRelate()));
        return Rest.ok(list);
    }
}
