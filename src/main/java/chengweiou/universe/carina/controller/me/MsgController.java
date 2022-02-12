package chengweiou.universe.carina.controller.me;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.manager.PushManager;
import chengweiou.universe.carina.model.Push;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.service.message.MsgService;
import chengweiou.universe.carina.service.person.PersonDio;

@RestController("meMsgController")
@RequestMapping("me")
public class MsgController {
    @Autowired
    private MsgService service;
    @Autowired
    private PushManager pushManager;
    @Autowired
    private PersonDio personDio;

    @PostMapping("/msg")
    public Rest<Long> save(History e, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, FailException, ProjException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("history.room", e.getRoom()).isNotNull();
        Valid.check("history.room.id", e.getRoom().getId()).is().positive();
        Valid.check("history.v", e.getV()).is().lengthIn(100);
        // tip: 推送的时候需要名字
        e.setSender(personDio.findById(loginAccount.getPerson()));
        List<History> list = service.send(e);
        list.parallelStream().forEach(each -> {
            try {
                pushManager.pushAsync(Builder.set("person", each.getPerson()).set("name", each.getSender().getName()).set("content", each.getV()).to(new Push()));
            } catch (FailException e1) {
                e1.printStackTrace();
            }
        });
        return Rest.ok(list.get(0).getId());
    }

    @GetMapping("/msg")
    public Rest<List<History>> find(SearchCondition searchCondition, History sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException, FailException {
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
