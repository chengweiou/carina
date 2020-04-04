package com.chengweiou.universe.carina.service.person;

import com.chengweiou.universe.carina.model.entity.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

@Service
public class PersonTask {
    @Autowired
    private PersonDio dio;

    @Async
    public Future<Long> update(Person e) {
        long count = dio.update(e);
        return new AsyncResult<>(count);
    }
}
