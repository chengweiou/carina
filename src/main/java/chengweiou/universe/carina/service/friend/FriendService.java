package chengweiou.universe.carina.service.friend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.carina.model.entity.friend.Friend;

@Service
public class FriendService {
    @Autowired
    private FriendDio dio;

    public void saveOrUpdate(Friend e) throws FailException, ProjException {
        Friend indb = dio.findByKey(e);
        if (!indb.notNull()) {
            dio.save(e);
        } else {
            e.setId(indb.getId());
            dio.update(e);
        }
    }

}
