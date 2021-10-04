package chengweiou.universe.carina.service.person;


import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.dao.person.PersonDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class PersonDio {
    @Autowired
    private PersonDao dao;

    public void save(Person e) throws FailException {
        e.fillNotRequire();
        e.createAt();
        e.updateAt();
        Person.Dto dto = e.toDto();
        long count = dao.save(dto);
        if (count != 1) throw new FailException();
        e.setId(dto.getId());
    }

    public void delete(Person e) throws FailException {
        long count = dao.delete(e.toDto());
        if (count != 1) throw new FailException();
    }

    public long update(Person e) {
        e.updateAt();
        return dao.update(e.toDto());
    }

    public Person findById(Person e) {
        Person.Dto result = dao.findById(e.toDto());
        if (result == null) return Person.NULL;
        return result.toBean();
    }

    public long count(SearchCondition searchCondition, Person sample) {
        return dao.count(searchCondition, sample!=null ? sample.toDto() : null);
    }

    public List<Person> find(SearchCondition searchCondition, Person sample) {
        searchCondition.setDefaultSort("updateAt");
        List<Person.Dto> dtoList = dao.find(searchCondition, sample!=null ? sample.toDto() : null);
        List<Person> result = dtoList.stream().map(e -> e.toBean()).collect(Collectors.toList());
        return result;
    }

}
