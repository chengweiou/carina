package chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.Room;

import java.util.List;

public interface RoomService {
    void save(Room e) throws FailException;

    void delete(Room e) throws FailException;

    long update(Room e);

    Room findById(Room e);

    long count(SearchCondition searchCondition);
    List<Room> find(SearchCondition searchCondition);

    long count(SearchCondition searchCondition, Room sample);
    List<Room> find(SearchCondition searchCondition, Room sample);
}
