package chengweiou.universe.carina.service.friend;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendService {
    @Autowired
    private FriendDio dio;

    public void save(Friend e) throws FailException, ProjException {
        dio.save(e);
    }

    public void delete(Friend e) throws FailException {
        dio.delete(e);
    }

    public long update(Friend e) {
        return dio.update(e);
    }

    public void saveOrUpdate(Friend e) throws FailException, ProjException {
        Friend indb = dio.findByKey(e);
        if (!indb.notNull()) {
            dio.save(e);
        } else {
            e.setId(indb.getId());
            dio.update(e);
        }
    }

    public Friend findById(Friend e) {
        return dio.findById(e);
    }
    public long countByKey(Friend e) {
        return dio.countByKey(e);
    }

    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }

    public List<Friend> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    public long count(SearchCondition searchCondition, Friend sample) {
        return dio.count(searchCondition, sample);
    }

    public List<Friend> find(SearchCondition searchCondition, Friend sample) {
        return dio.find(searchCondition, sample);
    }

}
