package com.chengweiou.universe.carina.service.room;


import chengweiou.universe.blackhole.exception.FailException;
import com.chengweiou.universe.carina.dao.room.PersonRoomRelateDao;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PersonRoomRelateDio {
    @Autowired
    private PersonRoomRelateDao dao;

    public void save(PersonRoomRelate e) throws FailException {
        e.fillNotRequire();
        e.fixLastMessage();
        e.createAt();
        e.updateAt();
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(PersonRoomRelate e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(PersonRoomRelate e) {
        e.updateAt();
        if (e.getLastMessage() != null) e.fixLastMessage();
        return dao.update(e);
    }

    public PersonRoomRelate findById(PersonRoomRelate e) {
        PersonRoomRelate result = dao.findById(e);
        if (result == null) return PersonRoomRelate.NULL;

        return result;
    }
    public PersonRoomRelate findByKey(PersonRoomRelate e) {
        PersonRoomRelate result = dao.findByKey(e);
        if (result == null) return PersonRoomRelate.NULL;

        return result;
    }

    public long count(SearchCondition searchCondition, PersonRoomRelate sample) {
        return dao.count(searchCondition, sample);
    }

    public List<PersonRoomRelate> find(SearchCondition searchCondition, PersonRoomRelate sample) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition, sample);
    }

}
