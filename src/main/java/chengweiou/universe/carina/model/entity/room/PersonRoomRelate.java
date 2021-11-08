package chengweiou.universe.carina.model.entity.room;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.blackhole.model.entity.DtoEntity;
import chengweiou.universe.blackhole.model.entity.DtoKey;
import chengweiou.universe.blackhole.model.entity.ServiceEntity;
import chengweiou.universe.carina.model.entity.person.Person;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonRoomRelate extends ServiceEntity {
    private Person person;
    private Room room;
    private String name;
    private String imgsrc;
    private Integer unread;
    private String lastMessage;
    private LocalDateTime lastMessageAt;

    public void fillNotRequire() {
        imgsrc = imgsrc!=null ? imgsrc : "";
        unread = unread!=null ? unread : 0;
        lastMessage = lastMessage!=null ? lastMessage : "";
        lastMessageAt = lastMessageAt!=null ? lastMessageAt : LocalDateTime.now();
    }

    public void fixLastMessage() {
        lastMessage = lastMessage.substring(0, Math.min(50, lastMessage.length()));
    }

    public static final PersonRoomRelate NULL = new PersonRoomRelate.Null();
    public static class Null extends PersonRoomRelate implements NullObj {
        @Override public Person getPerson() { return Person.NULL; }
        @Override public Room getRoom() { return Room.NULL; }
    }
    public Dto toDto() {
        Dto result = new Dto();
        BeanUtils.copyProperties(this, result);
        if (room != null) result.setRoomId(room.getId());
        if (person != null) result.setPersonId(person.getId());
        return result;
    }
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Dto extends DtoEntity {
        @DtoKey
        private Long personId;
        @DtoKey
        private Long roomId;
        private String name;
        private String imgsrc;
        private Integer unread;
        private String lastMessage;
        private LocalDateTime lastMessageAt;
        public PersonRoomRelate toBean() {
            PersonRoomRelate result = new PersonRoomRelate();
            BeanUtils.copyProperties(this, result);
            result.setRoom(Builder.set("id", roomId).to(new Room()));
            result.setPerson(Builder.set("id", personId).to(new Person()));
            return result;
        }
    }
}
