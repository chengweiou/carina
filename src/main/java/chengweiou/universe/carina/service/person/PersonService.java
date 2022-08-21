package chengweiou.universe.carina.service.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.service.room.PersonRoomRelateTask;

@Service
public class PersonService {
    @Autowired
    private PersonDio dio;
    @Autowired
    private PersonRoomRelateTask personRoomRelateTask;

    public boolean update(Person e) throws FailException {
        boolean success = dio.update(e) == 1;
        if (success) {
            if (e.getName() != null || e.getImgsrc() != null) personRoomRelateTask.updateSoloOtherByPerson(e);
        }
        return success;
    }

}
