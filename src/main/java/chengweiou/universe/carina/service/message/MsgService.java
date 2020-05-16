package chengweiou.universe.carina.service.message;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.Room;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface MsgService {
    void send(History e) throws FailException, ProjException;

    List<History> read(SearchCondition searchCondition, Person person, Room room) throws ExecutionException, InterruptedException;
}
