package chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.model.entity.room.RoomType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class PersonRoomRelateTask {
    @Autowired
    private PersonRoomRelateDio dio;
    @Autowired
    private RoomDio roomDio;

    @Async
    public Future<Long> update(PersonRoomRelate e) {
        long count = dio.update(e);
        return new AsyncResult<>(count);
    }

    /**
     * 当更新自己名字头像的时候，需要更新solo对方看到的名字头像
     * @param e
     * @return
     */
    public Future<Long> updateSoloOtherByPerson(Person person) {
        // 通过personid，获取房间id
        List<PersonRoomRelate> list = dio.findRoomId(
            Builder.set("limit", 0).to(new SearchCondition()),
            Builder.set("person", person).to(new PersonRoomRelate())
            );
        List<String> roomIdList = list.stream().map(e -> e.getRoom().getId().toString()).toList();
        // 过滤不是solo的
        List<Room> roomList = roomDio.findId(
            Builder.set("limit", 0).set("idList", roomIdList).to(new SearchCondition()),
            Builder.set("type", RoomType.SOLO).to(new Room())
            );
        List<String> soloRoomIdList = roomList.stream().map(e -> e.getId().toString()).toList();
        // 更新剩余房间，并且personid不是被修改的personid
        long count = dio.updateByOtherPerson(
            Builder.set("person", person).set("name", person.getName()).set("imgsrc", person.getImgsrc()).to(new PersonRoomRelate()),
            Builder.set("limit", 0).set("idList", soloRoomIdList).to(new SearchCondition())
            );
        return new AsyncResult<>(count);
    }
}
