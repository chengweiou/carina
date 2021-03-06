package chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.service.message.MsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("meMsgController")
@RequestMapping("me")
public class MsgController {
    @Autowired
    private MsgService service;

    @PostMapping("/msg")
    public Rest<Long> save(History e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, FailException, ProjException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("history.room", e.getRoom()).isNotNull();
        Valid.check("history.room.id", e.getRoom().getId()).is().positive();
        Valid.check("history.v", e.getV()).is().lengthIn(100);
        e.setSender(loginAccount.getPerson());
        List<History> list = service.send(e);
        return Rest.ok(list.get(0).getId());
    }

    @GetMapping("/msg")
    public Rest<List<History>> find(SearchCondition searchCondition, History sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("history.room", sample.getRoom()).isNotNull();
        Valid.check("history.room.id", sample.getRoom().getId()).is().positive();
        List<History> list = service.read(searchCondition, loginAccount.getPerson(), sample.getRoom());
        return Rest.ok(list);
    }

    @PostMapping("/msg/{id}/read")
    public Rest<Long> readById(History e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, FailException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("history.id", e.getId()).isNotNull();
        e.setPerson(loginAccount.getPerson());
        service.readById(e);
        return Rest.ok(null);
    }
}
