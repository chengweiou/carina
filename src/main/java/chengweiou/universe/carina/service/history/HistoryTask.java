package chengweiou.universe.carina.service.history;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.entity.history.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

@Service
public class HistoryTask {
    @Autowired
    private HistoryDio dio;

    @Async
    public Future<Long> delete(List<History> list) throws FailException {
        dio.deleteByIdList(list);
        return new AsyncResult<>(1L);
    }
}
