package chengweiou.universe.carina.service.history;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.entity.history.History;

@Service
public class HistoryTask {
    @Autowired
    private HistoryDio dio;

    @Async
    public CompletableFuture<Long> deleteByList(List<History> list) throws FailException {
        dio.deleteByIdList(list.stream().map(e -> e.getId().toString()).toList());
        return CompletableFuture.completedFuture(null);
    }
}
