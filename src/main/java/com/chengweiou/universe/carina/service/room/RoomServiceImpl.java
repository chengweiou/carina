package com.chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.room.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomDio dio;

    @Override
    public void save(Room e) throws FailException {
        dio.save(e);
    }

    @Override
    public void delete(Room e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(Room e) {
        return dio.update(e);
    }

    @Override
    public Room findById(Room e) {
        return dio.findById(e);
    }

    @Override
    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }
    @Override
    public List<Room> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    @Override
    public long count(SearchCondition searchCondition, Room sample) {
        return dio.count(searchCondition, sample);
    }
    @Override
    public List<Room> find(SearchCondition searchCondition, Room sample) {
        return dio.find(searchCondition, sample);
    }
}
