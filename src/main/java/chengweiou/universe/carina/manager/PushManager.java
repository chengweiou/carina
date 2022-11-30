package chengweiou.universe.carina.manager;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.carina.sdk.push.Push;
import chengweiou.universe.carina.sdk.push.PushService;

@Service
public class PushManager {
    @Autowired
    private PushService pushService;
    @Async
    public CompletableFuture<Long> pushAsync(Push e) throws FailException {
        Rest<Long> rest = pushService.push(e);
        if (rest.getCode() != BasicRestCode.OK) return CompletableFuture.completedFuture(0L);
        return CompletableFuture.completedFuture(rest.getData());
    }
}
