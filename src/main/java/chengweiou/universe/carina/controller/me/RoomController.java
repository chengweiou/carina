package chengweiou.universe.carina.controller.me;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.model.entity.room.RoomType;
import chengweiou.universe.carina.service.person.PersonService;
import chengweiou.universe.carina.service.room.PersonRoomRelateService;
import chengweiou.universe.carina.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController("meRoomController")
@RequestMapping("me")
public class RoomController {
    @Autowired
    private RoomService service;

    @GetMapping("/room")
    public Rest<Long> enterRoom(Room e, @RequestHeader("loginAccount") Account loginAccount)  throws ParamException, FailException {
        Valid.check("loginAccount.person", loginAccount.getPerson()).isNotNull();
        Valid.check("loginAccount.person.id", loginAccount.getPerson().getId()).is().positive();
        Valid.check("room.id | personIdList", e.getId(), e.getPersonIdList()).are().notAllNull();
        e.getPersonIdList().add(loginAccount.getPerson().getId());
        Room indb = service.enter(e);
        return Rest.ok(indb);
    }

    @DeleteMapping("/room/{id}")
    public Rest<Boolean> delete(Room e) throws ParamException, FailException {
        Valid.check("room.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }
}
