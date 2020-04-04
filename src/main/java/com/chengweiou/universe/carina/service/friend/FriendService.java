package com.chengweiou.universe.carina.service.friend;

import chengweiou.universe.blackhole.exception.FailException;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.friend.Friend;

import java.util.List;

public interface FriendService {
    void save(Friend e) throws FailException;

    void delete(Friend e) throws FailException;

    long update(Friend e);

    Friend findById(Friend e);

    long count(SearchCondition searchCondition);
    List<Friend> find(SearchCondition searchCondition);

    long count(SearchCondition searchCondition, Friend sample);
    List<Friend> find(SearchCondition searchCondition, Friend sample);

}
