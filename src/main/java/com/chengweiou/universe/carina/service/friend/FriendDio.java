package com.chengweiou.universe.carina.service.friend;


import chengweiou.universe.blackhole.exception.FailException;
import com.chengweiou.universe.carina.dao.friend.FriendDao;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.friend.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class FriendDio {
    @Autowired
    private FriendDao dao;

    public void save(Friend e) throws FailException {
        e.fillNotRequire();
        e.updateAt();
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(Friend e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(Friend e) {
        e.updateAt();
        return dao.update(e);
    }

    public Friend findById(Friend e) {
        Friend result = dao.findById(e);
        return result!=null ? result : Friend.NULL;
    }

    public long count(SearchCondition searchCondition, Friend sample) {
        return dao.count(searchCondition, sample);
    }

    public List<Friend> find(SearchCondition searchCondition, Friend sample) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition, sample);
    }
}
