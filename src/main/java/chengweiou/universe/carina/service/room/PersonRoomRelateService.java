package chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.service.person.PersonDio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void delete(PersonRoomRelate e) throws FailException {
        dio.delete(e);
    }

    public long update(PersonRoomRelate e) {
        return dio.update(e);
    }

    public PersonRoomRelate findById(PersonRoomRelate e) {
        return dio.findById(e);
    }
    public PersonRoomRelate findByKey(PersonRoomRelate e) {
        return dio.findByKey(e);
    }

    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }
    public List<PersonRoomRelate> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    public long count(SearchCondition searchCondition, PersonRoomRelate sample) {
        return dio.count(searchCondition, sample);
    }
    public List<PersonRoomRelate> find(SearchCondition searchCondition, PersonRoomRelate sample) {
        return dio.find(searchCondition, sample);
    }
}
