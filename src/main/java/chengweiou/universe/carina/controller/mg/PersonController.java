package chengweiou.universe.carina.controller.mg;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.person.PersonService;
import chengweiou.universe.carina.service.room.PersonRoomRelateTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("mgPersonController")
@RequestMapping("mg")
public class PersonController {
    @Autowired
    private PersonService service;
    @Autowired
    private PersonDio dio;
    @Autowired
    private PersonRoomRelateTask personRoomRelateTask;

    @PostMapping("/person")
    public Rest<Long> save(Person e) throws ParamException, FailException, ProjException {
        Valid.check("person.id", e.getId()).is().positive();
        Valid.check("person.name", e.getName()).is().lengthIn(100);
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/person/{id}")
    public Rest<Boolean> delete(Person e) throws ParamException, FailException {
        Valid.check("person.id", e.getId()).is().positive();
        dio.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/person/{id}")
    public Rest<Boolean> update(Person e) throws ParamException {
        Valid.check("person.id", e.getId()).is().positive();
        Valid.check("person.name | imgsrc | unread", e.getName(), e.getImgsrc(), e.getUnread()).are().notAllNull();
        boolean success = dio.update(e) == 1;
        if (success) {
            if (e.getName() != null || e.getImgsrc() != null) personRoomRelateTask.updateSoloOtherByPerson(e);
        }
        return Rest.ok(success);
    }

    @GetMapping("/person/{id}")
    public Rest<Person> findById(Person e) throws ParamException {
        Valid.check("person.id", e.getId()).is().positive();
        Person indb = dio.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/person/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = dio.count(searchCondition, null);
        return Rest.ok(count);
    }

    @GetMapping("/person")
    public Rest<List<Person>> find(SearchCondition searchCondition) {
        List<Person> list = dio.find(searchCondition, null);
        return Rest.ok(list);
    }
}
