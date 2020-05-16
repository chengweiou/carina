package chengweiou.universe.carina.model.entity.person;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class Person implements NotNullObj, Serializable {
    private Long id;
    private String name;
    private String imgsrc;
    private Integer unread;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public void fillNotRequire() {
        imgsrc = imgsrc!=null ? imgsrc : "";
        unread = unread!=null ? unread : 0;
    }

    public void createAt() {
        createAt = LocalDateTime.now(ZoneId.of("UTC"));
    }
    public void updateAt() {
        updateAt = LocalDateTime.now(ZoneId.of("UTC"));
    }

    public static final Person NULL = new Person.Null();
    public static class Null extends Person implements NullObj {
    }

}
