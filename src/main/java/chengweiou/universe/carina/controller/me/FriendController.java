package chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import chengweiou.universe.carina.service.friend.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("meFriendController")
@RequestMapping("me")
public class FriendController {
    @Autowired
    private FriendService service;

    @PostMapping("/friend")
    public Rest<Long> save(Friend e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, FailException, ProjException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("friend.target", e.getTarget()).isNotNull();
        Valid.check("friend.target.id", e.getTarget().getId()).is().positive();
        e.setPerson(loginAccount.getPerson());
        service.saveOrUpdate(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/friend/{id}")
    public Rest<Boolean> delete(Friend e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, FailException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("friend.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }

    @GetMapping("/friend/{id}")
    public Rest<Friend> findById(Friend e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("friend.id", e.getId()).is().positive();
        Friend indb = service.findById(e);
        return Rest.ok(indb);
    }
    @GetMapping("/friend/check")
    public Rest<Friend> check(Friend e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("friend.target", e.getTarget()).isNotNull();
        Valid.check("friend.target.id", e.getTarget().getId()).is().positive();
        e.setPerson(loginAccount.getPerson());
        long count = service.countByKey(e);
        return Rest.ok(count == 1);
    }

    @GetMapping("/friend/count")
    public Rest<Long> count(SearchCondition searchCondition, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        long count = service.count(searchCondition, Builder.set("person", loginAccount.getPerson()).to(new Friend()));
        return Rest.ok(count);
    }

    @GetMapping("/friend")
    public Rest<List<Friend>> find(SearchCondition searchCondition, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        List<Friend> list = service.find(searchCondition, Builder.set("person", loginAccount.getPerson()).to(new Friend()));
        return Rest.ok(list);
    }
}
