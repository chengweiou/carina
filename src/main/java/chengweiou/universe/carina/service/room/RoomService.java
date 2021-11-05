package chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.config.ProjConfig;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.model.entity.room.RoomType;
import chengweiou.universe.carina.service.history.HistoryDio;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.person.PersonTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired
    private RoomDio dio;
    @Autowired
    private PersonDio personDio;
    @Autowired
    private PersonRoomRelateDio personRoomRelateDio;
    @Autowired
    private HistoryDio historyDio;
    @Autowired
    private PersonTask personTask;
    @Autowired
    private PersonRoomRelateTask personRoomRelateTask;
    @Autowired
    private ProjConfig config;

    public void save(Room e) throws FailException, ProjException {
        dio.save(e);
    }

    public void delete(Room e) throws FailException {
        dio.delete(e);
    }

    public long update(Room e) {
        return dio.update(e);
    }

    public Room findById(Room e) {
        return dio.findById(e);
    }

    public Room findByKey(Room e) {
        e.setPersonIdList(e.getPersonIdList().stream().distinct().sorted().collect(Collectors.toList()));
        e.setType(e.getPersonIdList().size()==2 ? RoomType.SOLO : RoomType.GROUP);
        return dio.findByKey(e);
    }

    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }
    public List<Room> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    public long count(SearchCondition searchCondition, Room sample) {
        return dio.count(searchCondition, sample);
    }
    public List<Room> find(SearchCondition searchCondition, Room sample) {
        return dio.find(searchCondition, sample);
    }

    public Room enter(Room e) throws FailException, ProjException {
        // 旧房间
        if (e.getId() != null) return dio.findById(e);
        e.setPersonIdList(e.getPersonIdList().stream().distinct().sorted().collect(Collectors.toList()));
        e.setType(e.getPersonIdList().size()==2 ? RoomType.SOLO : RoomType.GROUP);
        Room indb = dio.findByKey(e);
        if (indb.notNull()) return indb;
        // 新房间
        return createSolo(e);
    }

    private Room createSolo(Room e) throws FailException, ProjException {
        e.setType(RoomType.SOLO);
        dio.save(e);
        List<Person> personList = personDio.find(
                Builder.set("idList", e.getPersonIdList().stream().map(personId->personId.toString()).collect(Collectors.toList())).to(new SearchCondition()),
                null);
        personRoomRelateDio.save(Builder.set("person", personList.get(0)).set("room", e).set("name", personList.get(1).getName()).set("imgsrc", personList.get(1).getImgsrc()).to(new PersonRoomRelate()));
        personRoomRelateDio.save(Builder.set("person", personList.get(1)).set("room", e).set("name", personList.get(0).getName()).set("imgsrc", personList.get(0).getImgsrc()).to(new PersonRoomRelate()));
        return e;
    }

    public Room createGroup(Room e, String name, String imgsrc) throws FailException, ProjException {
        e.setType(RoomType.GROUP);
        dio.save(e);
        List<Person> personList = personDio.find(
                Builder.set("idList", e.getPersonIdList().stream().map(personId->personId.toString()).collect(Collectors.toList())).to(new SearchCondition()),
                null);
        for (Person person : personList) {
            PersonRoomRelate relate = Builder.set("person", person).set("room", e).set("name", name).set("imgsrc", imgsrc).to(new PersonRoomRelate());
            personRoomRelateDio.save(relate);
        }
        return e;
    }

    public void leaveRoom(Person person, Room room) {
        if (config.getServerHistory()) {
            historyDio.updateUnreadByRoomAndPerson(Builder.set("person", person).set("room", room).set("unread", false).to(new History()));
        } else {
            List<History> list = historyDio.find(Builder.set("limit", 0).to(new SearchCondition()), Builder.set("person", person).set("room", room).to(new History()));
            historyDio.delete(list);
        }
        PersonRoomRelate relate = personRoomRelateDio.findByKey(Builder.set("person", person).set("room", room).to(new PersonRoomRelate()));
        relate.setUnread(0);
        personRoomRelateTask.update(relate);
        long personUnread = historyDio.count(new SearchCondition(), Builder.set("person", person).set("unread", true).to(new History()));
        person.setUnread(Math.toIntExact(personUnread));
        personTask.update(person);
    }
}
