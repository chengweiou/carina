package com.chengweiou.universe.carina.controller.mg;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import com.chengweiou.universe.carina.service.room.PersonRoomRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("mgPersonRoomRelateController")
@RequestMapping("mg")
public class PersonRoomRelateController {
    @Autowired
    private PersonRoomRelateService service;

    @PostMapping("/personRoomRelate")
    public Rest<Long> save(PersonRoomRelate e) throws ParamException, FailException, ProjException {
        Valid.check("personRoomRelate.person", e.getPerson()).isNotNull();
        Valid.check("personRoomRelate.person.id", e.getPerson().getId()).is().positive();
        Valid.check("personRoomRelate.room", e.getPerson()).isNotNull();
        Valid.check("personRoomRelate.room.id", e.getPerson().getId()).is().positive();
        Valid.check("personRoomRelate.name", e.getName()).nullable().lengthIn(100);
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/personRoomRelate/{id}")
    public Rest<Boolean> delete(PersonRoomRelate e) throws ParamException, FailException {
        Valid.check("personRoomRelate.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/personRoomRelate/{id}")
    public Rest<Boolean> update(PersonRoomRelate e) throws ParamException {
        Valid.check("personRoomRelate.id", e.getId()).is().positive();
        Valid.check("personRoomRelate.person | room | name | imgsrc | unread | lastMessage",
                e.getPerson(), e.getRoom(), e.getName(), e.getImgsrc(), e.getUnread(), e.getLastMessage()).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/personRoomRelate/{id}")
    public Rest<PersonRoomRelate> findById(PersonRoomRelate e) throws ParamException {
        Valid.check("personRoomRelate.id", e.getId()).is().positive();
        PersonRoomRelate indb = service.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/personRoomRelate/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = service.count(searchCondition);
        return Rest.ok(count);
    }

    @GetMapping("/personRoomRelate")
    public Rest<List<PersonRoomRelate>> find(SearchCondition searchCondition) {
        List<PersonRoomRelate> list = service.find(searchCondition);
        return Rest.ok(list);
    }
}
