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
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.service.history.HistoryDio;

@RestController("meHistoryController")
@RequestMapping("me")
public class HistoryController {
    @Autowired
    private HistoryDio dio;

    @GetMapping("/history/count")
    public Rest<Long> count(SearchCondition searchCondition, History sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        long count = dio.count(searchCondition, Builder.set("person", loginAccount.getPerson()).to(sample));
        return Rest.ok(count);
    }

    @GetMapping("/history")
    public Rest<List<History>> find(SearchCondition searchCondition, History sample, @RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        List<History> list = dio.find(searchCondition, Builder.set("person", loginAccount.getPerson()).to(sample));
        return Rest.ok(list);
    }

}
