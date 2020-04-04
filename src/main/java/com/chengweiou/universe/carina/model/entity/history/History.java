package com.chengweiou.universe.carina.model.entity.history;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import com.chengweiou.universe.carina.model.entity.person.Person;
import com.chengweiou.universe.carina.model.entity.room.Room;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class History implements NotNullObj, Serializable {
    private Long id;
    private Room room;
    private Person person;
    private Person sender;
    private HistoryType type;
    private String v;
    private LocalDateTime updateAt;

    public void fillNotRequire() {
        type = type != null ? type : HistoryType.TEXT;
    }

    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final History NULL = new History.Null();
    public static class Null extends History implements NullObj {
        @Override public Person getPerson() { return Person.NULL; }
        @Override public Person getSender() { return Person.NULL; }
    }

}