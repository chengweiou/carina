package chengweiou.universe.carina.controller.mg;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ParamException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.service.room.RoomDio;

@RestController
@RequestMapping("mg")
public class RoomControllerMg {
    @Autowired
    private RoomDio dio;

    @PostMapping("/room")
    public Rest<Long> save(Room e) throws ParamException, FailException, ProjException {
        Valid.check("room.personIdList", e.getPersonIdList()).isNotNull();
        dio.save(e);
        return Rest.ok(e.getId());
    }

    @DeleteMapping("/room/{id}")
    public Rest<Boolean> delete(Room e) throws ParamException, FailException {
        Valid.check("room.id", e.getId()).is().positive();
        dio.delete(e);
        return Rest.ok(true);
    }
    @PutMapping("/room/{id}")
    public Rest<Boolean> update(Room e) throws ParamException, FailException {
        Valid.check("room.id", e.getId()).is().positive();
        Valid.check("room.personIdList", e.getPersonIdList()).isNotNull();
        boolean success = dio.update(e) == 1;
        return Rest.ok(success);
    }

    @GetMapping("/room/{id}")
    public Rest<Room> findById(Room e) throws ParamException {
        Valid.check("room.id", e.getId()).is().positive();
        Room indb = dio.findById(e);
        return Rest.ok(indb);
    }

    @GetMapping("/room/count")
    public Rest<Long> count(SearchCondition searchCondition) {
        long count = dio.count(searchCondition, null);
        return Rest.ok(count);
    }

    @GetMapping("/room")
    public Rest<List<Room>> find(SearchCondition searchCondition) {
        List<Room> list = dio.find(searchCondition, null);
        return Rest.ok(list);
    }
}
