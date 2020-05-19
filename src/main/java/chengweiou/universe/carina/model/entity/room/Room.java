package chengweiou.universe.carina.model.entity.room;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Data
public class Room implements NotNullObj, Serializable {
    private Long id;
    private RoomType type;
    private String personIdListString;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    // not in db
    private List<Long> personIdList;

    public void fillNotRequire() {
        personIdList = personIdList != null ? personIdList : new ArrayList<>();
    }

    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final Room NULL = new Room.Null();
    public static class Null extends Room implements NullObj {
    }

}