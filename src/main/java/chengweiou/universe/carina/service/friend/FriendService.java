package chengweiou.universe.carina.service.friend;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;

import java.util.List;

public interface FriendService {
    void save(Friend e) throws FailException;

    void delete(Friend e) throws FailException;

    long update(Friend e);

    void saveOrUpdate(Friend e) throws FailException;

    Friend findById(Friend e);
    long countByKey(Friend e);

    long count(SearchCondition searchCondition);
    List<Friend> find(SearchCondition searchCondition);

    long count(SearchCondition searchCondition, Friend sample);
    List<Friend> find(SearchCondition searchCondition, Friend sample);


}
