package chengweiou.universe.carina.controller.mg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import chengweiou.universe.carina.service.friend.FriendDio;

@RestController
@RequestMapping("mg")
public class FriendControllerMg {
    @Autowired
    private FriendDio dio;

    @PostMapping("/friend")
    public Rest<Long> save(Friend e) throws ParamException, FailException, ProjException {
        Valid.check("friend.person", e.getPerson()).isNotNull();
        Valid.check("friend.person.id", e.getPerson().getId()).is().positive();
        Valid.check("friend.target", e.getTarget()).isNotNull();
        Valid.check("friend.target.id", e.getTarget().getId()).is().positive();
        dio.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/friend/{id}")
    public Rest<Boolean> delete(Friend e) throws ParamException, FailException {
        Valid.check("friend.id", e.getId()).is().positive();
        dio.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/friend/{id}")
    public Rest<Boolean> update(Friend e) throws ParamException, FailException {
        Valid.check("friend.id", e.getId()).is().positive();
        Valid.check("friend.person | target", e.getPerson(), e.getTarget()).are().notAllNull();
        boolean success = dio.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/friend/{id}")
    public Rest<Friend> findById(Friend e) throws ParamException {
        Valid.check("friend.id", e.getId()).is().positive();
        Friend indb = dio.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/friend/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = dio.count(searchCondition, null);
        return Rest.ok(count);
    }

    @GetMapping("/friend")
    public Rest<List<Friend>> find(SearchCondition searchCondition) {
        List<Friend> list = dio.find(searchCondition, null);
        return Rest.ok(list);
    }
}
