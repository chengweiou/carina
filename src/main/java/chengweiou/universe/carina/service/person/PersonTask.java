package chengweiou.universe.carina.service.person;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.entity.person.Person;

@Service
public class PersonTask {
    @Autowired
    private PersonDio dio;

    @Async
    public CompletableFuture<Long> update(Person e) throws FailException {
        long count = dio.update(e);
        return CompletableFuture.completedFuture(count);
    }
}
