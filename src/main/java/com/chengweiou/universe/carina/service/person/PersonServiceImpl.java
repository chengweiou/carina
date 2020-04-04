package com.chengweiou.universe.carina.service.person;

import chengweiou.universe.blackhole.exception.FailException;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonDio dio;

    @Override
    public void save(Person e) throws FailException {
        e.setUnread(0);
        dio.save(e);
    }

    @Override
    public void delete(Person e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(Person e) {
        return dio.update(e);
    }

    @Override
    public Person findById(Person e) {
        return dio.findById(e);
    }

    @Override
    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }
    @Override
    public List<Person> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    @Override
    public long count(SearchCondition searchCondition, Person sample) {
        return dio.count(searchCondition, sample);
    }
    @Override
    public List<Person> find(SearchCondition searchCondition, Person sample) {
        return dio.find(searchCondition, sample);
    }
}
