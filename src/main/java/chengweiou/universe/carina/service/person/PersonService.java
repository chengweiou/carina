package chengweiou.universe.carina.service.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.entity.person.Person;

@Service
public class PersonService {
    @Autowired
    private PersonDio dio;

    public void save(Person e) throws FailException {
        e.setUnread(0);
        dio.save(e);
    }

}
