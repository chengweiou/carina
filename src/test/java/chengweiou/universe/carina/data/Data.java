package chengweiou.universe.carina.data;

import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.friend.Friend;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.service.friend.FriendDio;
import chengweiou.universe.carina.service.history.HistoryDio;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import chengweiou.universe.carina.service.room.RoomDio;

@Component
public class Data {
    @Autowired
    private PersonDio personDio;
    public List<Person> personList;

    @Autowired
    private RoomDio roomDio;
    public List<Room> roomList;

    @Autowired
    private PersonRoomRelateDio personRoomRelateDio;
    public List<PersonRoomRelate> personRoomRelateList;

    @Autowired
    private HistoryDio historyDio;
    public List<History> historyList;

    @Autowired
    private FriendDio friendDio;
    public List<Friend> friendList;

    public void init() {
        personList = personDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Person::getId)).toList();
        roomList = roomDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Room::getId)).toList();
        personRoomRelateList = personRoomRelateDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(PersonRoomRelate::getId)).toList();
        historyList = historyDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(History::getId)).toList();
        friendList = friendDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Friend::getId)).toList();

    }

    /**
     * 放在 @AfterEach 可用于发现哪个测试的导致数据数量变化
     */
    public void check() {
        long historyCount = historyDio.count(new SearchCondition(), null);
        Assertions.assertEquals(historyList.size(), historyCount);
        // assert historyCount == historyList.size();
    }

}
