package chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.service.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("mePersonController")
@RequestMapping("me")
public class PersonController {
    @Autowired
    private PersonService service;

    @GetMapping("")
    public Rest<Person> findById(@RequestHeader("loginAccount") Account loginAccount) throws ParamException {
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Person indb = service.findById(loginAccount.getPerson());
        return Rest.ok(indb);
    }
}
