package com.chengweiou.universe.carina.service.history;

import com.chengweiou.universe.carina.model.entity.history.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class HistoryTask {
    @Autowired
    private HistoryService service;

    @Async
    public Future<Long> delete(List<History> list) {
        long count = service.delete(list);
        return new AsyncResult<>(count);
    }
}
