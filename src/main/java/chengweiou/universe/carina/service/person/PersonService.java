package chengweiou.universe.carina.service.person;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonDio dio;

    public void save(Person e) throws FailException {
        e.setUnread(0);
        dio.save(e);
    }

    public void delete(Person e) throws FailException {
        dio.delete(e);
    }

    public long update(Person e) {
        return dio.update(e);
    }

    public Person findById(Person e) {
        return dio.findById(e);
    }

    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }
    public List<Person> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    public long count(SearchCondition searchCondition, Person sample) {
        return dio.count(searchCondition, sample);
    }
    public List<Person> find(SearchCondition searchCondition, Person sample) {
        return dio.find(searchCondition, sample);
    }
}
