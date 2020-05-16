package chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;

import java.util.List;

public interface PersonRoomRelateService {
    void save(PersonRoomRelate e) throws FailException;

    void delete(PersonRoomRelate e) throws FailException;

    long update(PersonRoomRelate e);

    PersonRoomRelate findById(PersonRoomRelate e);
    PersonRoomRelate findByKey(PersonRoomRelate e);

    long count(SearchCondition searchCondition);

    List<PersonRoomRelate> find(SearchCondition searchCondition);
    long count(SearchCondition searchCondition, PersonRoomRelate sample);

    List<PersonRoomRelate> find(SearchCondition searchCondition, PersonRoomRelate sample);
}
