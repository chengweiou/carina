package com.chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import com.chengweiou.universe.carina.base.converter.Account;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.history.History;
import com.chengweiou.universe.carina.service.history.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("meHistoryController")
@RequestMapping("me")
public class HistoryController {
    @Autowired
    private HistoryService service;

    @GetMapping("/history/count")
    public Rest<Long> count(SearchCondition searchCondition, History sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        long count = service.count(searchCondition, Builder.set("person", loginAccount.getPerson()).to(sample));
        return Rest.ok(count);
    }

    @GetMapping("/history")
    public Rest<List<History>> find(SearchCondition searchCondition, History sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        List<History> list = service.find(searchCondition, Builder.set("person", loginAccount.getPerson()).to(sample));
        return Rest.ok(list);
    }

}
