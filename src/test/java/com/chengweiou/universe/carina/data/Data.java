package com.chengweiou.universe.carina.data;

import com.chengweiou.universe.carina.model.SearchCondition;
import com.chengweiou.universe.carina.model.entity.friend.Friend;
import com.chengweiou.universe.carina.model.entity.history.History;
import com.chengweiou.universe.carina.model.entity.person.Person;
import com.chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import com.chengweiou.universe.carina.model.entity.room.Room;
import com.chengweiou.universe.carina.service.friend.FriendDio;
import com.chengweiou.universe.carina.service.history.HistoryDio;
import com.chengweiou.universe.carina.service.person.PersonDio;
import com.chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import com.chengweiou.universe.carina.service.room.RoomDio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        personList = personDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Person::getId)).collect(Collectors.toList());
        roomList = roomDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Room::getId)).collect(Collectors.toList());
        personRoomRelateList = personRoomRelateDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(PersonRoomRelate::getId)).collect(Collectors.toList());
        historyList = historyDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(History::getId)).collect(Collectors.toList());
        friendList = friendDio.find(new SearchCondition(), null).stream().sorted(Comparator.comparingLong(Friend::getId)).collect(Collectors.toList());

    }
}
