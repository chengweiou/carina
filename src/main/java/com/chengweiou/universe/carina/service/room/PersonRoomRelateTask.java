package com.chengweiou.universe.carina.service.room;

import com.chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class PersonRoomRelateTask {
    @Autowired
    private PersonRoomRelateDio dio;

    @Async
    public Future<Long> update(PersonRoomRelate e) {
        long count = dio.update(e);
        return new AsyncResult<>(count);
    }
}
