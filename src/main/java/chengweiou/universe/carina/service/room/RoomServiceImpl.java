package chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.model.entity.room.RoomType;
import chengweiou.universe.carina.service.person.PersonDio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomDio dio;
    @Autowired
    private PersonDio personDio;
    @Autowired
    private PersonRoomRelateDio personRoomRelateDio;

    @Override
    public void save(Room e) throws FailException {
        dio.save(e);
    }

    @Override
    public void delete(Room e) throws FailException {
        dio.delete(e);
    }

    @Override
    public long update(Room e) {
        return dio.update(e);
    }

    @Override
    public Room findById(Room e) {
        return dio.findById(e);
    }

    @Override
    public long count(SearchCondition searchCondition) {
        return dio.count(searchCondition, null);
    }
    @Override
    public List<Room> find(SearchCondition searchCondition) {
        return dio.find(searchCondition, null);
    }

    @Override
    public long count(SearchCondition searchCondition, Room sample) {
        return dio.count(searchCondition, sample);
    }
    @Override
    public List<Room> find(SearchCondition searchCondition, Room sample) {
        return dio.find(searchCondition, sample);
    }

    @Override
    public Room enter(Room e) throws FailException {
        // 旧房间
        if (e.getId() != null) return dio.findById(e);
        e.setPersonIdList(e.getPersonIdList().stream().distinct().sorted().collect(Collectors.toList()));
        e.setType(e.getPersonIdList().size()==2 ? RoomType.SOLO : RoomType.GROUP);
        Room indb = dio.findByKey(e);
        if (indb.notNull()) return indb;
        // 新房间
        return createSolo(e);
    }

    private Room createSolo(Room e) throws FailException {
        e.setType(RoomType.SOLO);
        dio.save(e);
        List<Person> personList = personDio.find(
                Builder.set("idList", e.getPersonIdList().stream().map(personId->personId.toString()).collect(Collectors.toList())).to(new SearchCondition()),
                null);
        personRoomRelateDio.save(Builder.set("person", personList.get(0)).set("room", e).set("name", personList.get(1).getName()).set("imgsrc", personList.get(1).getImgsrc()).to(new PersonRoomRelate()));
        personRoomRelateDio.save(Builder.set("person", personList.get(1)).set("room", e).set("name", personList.get(0).getName()).set("imgsrc", personList.get(0).getImgsrc()).to(new PersonRoomRelate()));
        return e;
    }

    @Override
    public Room createGroup(Room e, String name, String imgsrc) throws FailException {
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
}
