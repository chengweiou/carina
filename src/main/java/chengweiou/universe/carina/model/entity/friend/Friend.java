package chengweiou.universe.carina.model.entity.friend;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.carina.model.entity.person.Person;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class Friend implements NotNullObj, Serializable {
    private Long id;
    private Person person;
    private Person target;
    private LocalDateTime updateAt;

    public void fillNotRequire() {

    }

    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final Friend NULL = new Friend.Null();
    public static class Null extends Friend implements NullObj {
        @Override public Person getPerson() { return Person.NULL; }
        @Override public Person getTarget() { return Person.NULL; }
    }

}