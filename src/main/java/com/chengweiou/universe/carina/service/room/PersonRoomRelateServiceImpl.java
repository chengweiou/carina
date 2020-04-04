package com.chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.person.Person;
import com.chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import com.chengweiou.universe.carina.service.person.PersonDio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonRoomRelateServiceImpl implements PersonRoomRelateService {
    @Autowired
    private PersonRoomRelateDio dio;
    @Autowired
    private PersonDio personDio;

    @Override
    public void save(PersonRoomRelate e) throws FailException {
        if (e.getName() == null) {
            Person person = personDio.findById(e.getPerson());
            e.setName(person.getName());
        }
        dio.save(e);
    }

    @Override
    public void delete(PersonRoomRelate e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(PersonRoomRelate e) {
        return dio.update(e);
    }

    @Override
    public PersonRoomRelate findById(PersonRoomRelate e) {
        return dio.findById(e);
    }
    @Override
    public PersonRoomRelate findByKey(PersonRoomRelate e) {
        return dio.findByKey(e);
    }

    @Override
    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }
    @Override
    public List<PersonRoomRelate> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    @Override
    public long count(SearchCondition searchCondition, PersonRoomRelate sample) {
        return dio.count(searchCondition, sample);
    }
    @Override
    public List<PersonRoomRelate> find(SearchCondition searchCondition, PersonRoomRelate sample) {
        return dio.find(searchCondition, sample);
    }
}
