package chengweiou.universe.carina.service.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.service.person.PersonDio;

@Service
public class PersonRoomRelateService {
    @Autowired
    private PersonRoomRelateDio dio;
    @Autowired
    private PersonDio personDio;

    public void save(PersonRoomRelate e) throws FailException, ProjException {
        if (e.getName() == null) {
            Person person = personDio.findById(e.getPerson());
            e.setName(person.getName());
        }
        dio.save(e);
    }

}
