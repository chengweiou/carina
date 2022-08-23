package chengweiou.universe.carina.sdk.push;

import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.carina.model.entity.person.Person;
import lombok.Data;

import java.io.Serializable;

@Data
public class Push implements NotNullObj, Serializable {
    private Person person;
    private String topic;
    private String name;
    private String content;
    private String pushSpecType;
    private Integer num;
    private PushInApp pushInApp; // 给前端使用
    public static final Push NULL = new Push.Null();
    public static class Null extends Push implements NullObj {
    }
    private PushSpec pushSpec;
}
