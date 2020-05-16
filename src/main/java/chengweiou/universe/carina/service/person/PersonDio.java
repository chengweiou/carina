package chengweiou.universe.carina.service.person;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.dao.person.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PersonDio {
    @Autowired
    private PersonDao dao;

    public void save(Person e) throws FailException {
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        long count = dao.save(e);
        if (count != 1) throw new FailException();
    }

    public void delete(Person e) throws FailException {
        long count = dao.delete(e);
        if (count != 1) throw new FailException();
    }

    public long update(Person e) {
        e.updateAt();
        return dao.update(e);
    }

    public Person findById(Person e) {
        Person result = dao.findById(e);
        return result != null ? result : Person.NULL;
    }

    public long count(SearchCondition searchCondition, Person sample) {
        return dao.count(searchCondition, sample);
    }

    public List<Person> find(SearchCondition searchCondition, Person sample) {
        searchCondition.setDefaultSort("updateAt");
        return dao.find(searchCondition, sample);
    }


}
