package chengweiou.universe.carina.controller.mg;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mg")
public class PersonRoomRelateControllerMg {
    @Autowired
    private PersonRoomRelateService service;
    @Autowired
    private PersonRoomRelateDio dio;

    @PostMapping("/personRoomRelate")
    public Rest<Long> save(PersonRoomRelate e) throws ParamException, FailException, ProjException {
        Valid.check("personRoomRelate.person", e.getPerson()).isNotNull();
        Valid.check("personRoomRelate.person.id", e.getPerson().getId()).is().positive();
        Valid.check("personRoomRelate.room", e.getPerson()).isNotNull();
        Valid.check("personRoomRelate.room.id", e.getPerson().getId()).is().positive();
        Valid.check("personRoomRelate.name", e.getName()).nullable().lengthIn(100);
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/personRoomRelate/{id}")
    public Rest<Boolean> delete(PersonRoomRelate e) throws ParamException, FailException {
        Valid.check("personRoomRelate.id", e.getId()).is().positive();
        dio.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/personRoomRelate/{id}")
    public Rest<Boolean> update(PersonRoomRelate e) throws ParamException {
        Valid.check("personRoomRelate.id", e.getId()).is().positive();
        Valid.check("personRoomRelate.person | room | name | imgsrc | unread | lastMessage | lastMessageAt",
                e.getPerson(), e.getRoom(), e.getName(), e.getImgsrc(), e.getUnread(), e.getLastMessage(), e.getLastMessageAt()).are().notAllNull();
        boolean success = dio.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/personRoomRelate/{id}")
    public Rest<PersonRoomRelate> findById(PersonRoomRelate e) throws ParamException {
        Valid.check("personRoomRelate.id", e.getId()).is().positive();
        PersonRoomRelate indb = dio.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/personRoomRelate/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = dio.count(searchCondition, null);
        return Rest.ok(count);
    }

    @GetMapping("/personRoomRelate")
    public Rest<List<PersonRoomRelate>> find(SearchCondition searchCondition) {
        List<PersonRoomRelate> list = dio.find(searchCondition, null);
        return Rest.ok(list);
    }
}
