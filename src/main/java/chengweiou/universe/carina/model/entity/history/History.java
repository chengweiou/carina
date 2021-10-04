package chengweiou.universe.carina.model.entity.history;

import org.springframework.beans.BeanUtils;

import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.carina.base.entity.DtoEntity;
import chengweiou.universe.carina.base.entity.DtoKey;
import chengweiou.universe.carina.base.entity.ServiceEntity;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.Room;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class History extends ServiceEntity {
    private Room room;
    private Person person;
    private Person sender;
    private HistoryType type;
    private String v;
    private Boolean unread;

    private String extra; // not indb 用于原样返回给前端

    public void fillNotRequire() {
        type = type != null ? type : HistoryType.TEXT;
        unread = unread != null ? unread : true;
    }

    public static final History NULL = new History.Null();
    public static class Null extends History implements NullObj {
        @Override public Room getRoom() { return Room.NULL; }
        @Override public Person getPerson() { return Person.NULL; }
        @Override public Person getSender() { return Person.NULL; }
    }
    public Dto toDto() {
        Dto result = new Dto();
        BeanUtils.copyProperties(this, result);
        if (room != null) result.setRoomId(room.getId());
        if (person != null) result.setPersonId(person.getId());
        if (sender != null) result.setSenderId(sender.getId());
        return result;
    }
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Dto extends DtoEntity {
        private Long roomId;
        private Long personId;
        private Long senderId;
        private HistoryType type;
        private String v;
        private Boolean unread;

        public History toBean() {
            History result = new History();
            BeanUtils.copyProperties(this, result);
            result.setRoom(Builder.set("id", roomId).to(new Room()));
            result.setPerson(Builder.set("id", personId).to(new Person()));
            result.setSender(Builder.set("id", senderId).to(new Person()));
            return result;
        }
    }
}
