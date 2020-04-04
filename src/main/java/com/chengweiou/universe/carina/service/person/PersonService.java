package com.chengweiou.universe.carina.service.person;

import chengweiou.universe.blackhole.exception.FailException;
import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.person.Person;

import java.util.List;

public interface PersonService {
    void save(Person e) throws FailException;

    void delete(Person e) throws FailException;

    long update(Person e);

    Person findById(Person e);

    long count(SearchCondition searchCondition);
    List<Person> find(SearchCondition searchCondition);

    long count(SearchCondition searchCondition, Person sample);
    List<Person> find(SearchCondition searchCondition, Person sample);
}
