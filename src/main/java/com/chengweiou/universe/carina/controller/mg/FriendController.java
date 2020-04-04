package com.chengweiou.universe.carina.controller.mg;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.friend.Friend;
import com.chengweiou.universe.carina.service.friend.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("mgFriendController")
@RequestMapping("mg")
public class FriendController {
    @Autowired
    private FriendService service;

    @PostMapping("/friend")
    public Rest<Long> save(Friend e) throws ParamException, FailException, ProjException {
        Valid.check("friend.person", e.getPerson()).isNotNull();
        Valid.check("friend.person.id", e.getPerson().getId()).is().positive();
        Valid.check("friend.target", e.getTarget()).isNotNull();
        Valid.check("friend.target.id", e.getTarget().getId()).is().positive();
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/friend/{id}")
    public Rest<Boolean> delete(Friend e) throws ParamException, FailException {
        Valid.check("friend.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/friend/{id}")
    public Rest<Boolean> update(Friend e) throws ParamException {
        Valid.check("friend.id", e.getId()).is().positive();
        Valid.check("friend.person | target", e.getPerson(), e.getTarget()).are().notAllNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/friend/{id}")
    public Rest<Friend> findById(Friend e) throws ParamException {
        Valid.check("friend.id", e.getId()).is().positive();
        Friend indb = service.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/friend/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = service.count(searchCondition);
        return Rest.ok(count);
    }

    @GetMapping("/friend")
    public Rest<List<Friend>> find(SearchCondition searchCondition) {
        List<Friend> list = service.find(searchCondition);
        return Rest.ok(list);
    }
}
