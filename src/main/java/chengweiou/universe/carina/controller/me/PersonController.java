package chengweiou.universe.carina.controller.me;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.service.person.PersonDio;

@RestController("mePersonController")
@RequestMapping("me")
public class PersonController {
    @Autowired
    private PersonDio dio;

    @GetMapping("")
    public Rest<Person> findById(@RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Person indb = dio.findById(loginAccount.getPerson());
        return Rest.ok(indb);
    }

    // 用于显示聊天室内的名字，头像
    @GetMapping("/person")
    public Rest<List<Person>> find(SearchCondition searchCondition) {
        List<Person> list = dio.find(searchCondition, null);
        list.stream().forEach(e -> e.setUnread(null));
        return Rest.ok(list);
    }
}
