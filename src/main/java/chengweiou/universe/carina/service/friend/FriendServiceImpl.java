package chengweiou.universe.carina.service.friend;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    @Autowired
    private FriendDio dio;

    @Override
    public void save(Friend e) throws FailException {
        dio.save(e);
    }

    @Override
    public void delete(Friend e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(Friend e) {
        return dio.update(e);
    }

    @Override
    public void saveOrUpdate(Friend e) throws FailException {
        Friend indb = dio.findByKey(e);
        if (!indb.notNull()) {
            dio.save(e);
        } else {
            e.setId(indb.getId());
            dio.update(e);
        }
    }

    @Override
    public Friend findById(Friend e) {
        return dio.findById(e);
    }
    @Override
    public long countByKey(Friend e) {
        return dio.countByKey(e);
    }

    @Override
    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }

    @Override
    public List<Friend> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    @Override
    public long count(SearchCondition searchCondition, Friend sample) {
        return dio.count(searchCondition, sample);
    }

    @Override
    public List<Friend> find(SearchCondition searchCondition, Friend sample) {
        return dio.find(searchCondition, sample);
    }

}
