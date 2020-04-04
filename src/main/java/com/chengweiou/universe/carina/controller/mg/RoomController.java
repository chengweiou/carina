package com.chengweiou.universe.carina.controller.mg;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.room.Room;
import com.chengweiou.universe.carina.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("mgRoomController")
@RequestMapping("mg")
public class RoomController {
    @Autowired
    private RoomService service;

    @PostMapping("/room")
    public Rest<Long> save(Room e) throws ParamException, FailException, ProjException {
        Valid.check("room.personIdList", e.getPersonIdList()).isNotNull();
        service.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/room/{id}")
    public Rest<Boolean> delete(Room e) throws ParamException, FailException {
        Valid.check("room.id", e.getId()).is().positive();
        service.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/room/{id}")
    public Rest<Boolean> update(Room e) throws ParamException {
        Valid.check("room.id", e.getId()).is().positive();
        Valid.check("room.personIdList", e.getPersonIdList()).isNotNull();
        boolean success = service.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/room/{id}")
    public Rest<Room> findById(Room e) throws ParamException {
        Valid.check("room.id", e.getId()).is().positive();
        Room indb = service.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/room/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = service.count(searchCondition);
        return Rest.ok(count);
    }

    @GetMapping("/room")
    public Rest<List<Room>> find(SearchCondition searchCondition) {
        List<Room> list = service.find(searchCondition);
        return Rest.ok(list);
    }
}
