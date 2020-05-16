package chengweiou.universe.carina.model.entity.room;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.carina.model.entity.person.Person;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class PersonRoomRelate implements NotNullObj, Serializable {
    private Long id;
    private Person person;
    private Room room;
    private String name;
    private String imgsrc;
    private Integer unread;
    private String lastMessage;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void fillNotRequire() {
        imgsrc = imgsrc!=null ? imgsrc : "";
        unread = unread!=null ? unread : 0;
        lastMessage = lastMessage!=null ? lastMessage : "";
    }

    public void fixLastMessage() {
        lastMessage = lastMessage.substring(0, Math.min(50, lastMessage.length()));
    }
    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final PersonRoomRelate NULL = new PersonRoomRelate.Null();
    public static class Null extends PersonRoomRelate implements NullObj {
        @Override public Person getPerson() { return Person.NULL; }
        @Override public Room getRoom() { return Room.NULL; }
    }

}