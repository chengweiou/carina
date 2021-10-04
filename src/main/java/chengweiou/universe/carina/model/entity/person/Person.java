package chengweiou.universe.carina.model.entity.person;

import org.springframework.beans.BeanUtils;

import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.carina.base.entity.DtoEntity;
import chengweiou.universe.carina.base.entity.ServiceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends ServiceEntity {
    private String name;
    private String imgsrc;
    private Integer unread;

    public void fillNotRequire() {
        imgsrc = imgsrc!=null ? imgsrc : "";
        unread = unread!=null ? unread : 0;
    }

    public static final Person NULL = new Person.Null();
    public static class Null extends Person implements NullObj {
    }
    public Dto toDto() {
        Dto result = new Dto();
        BeanUtils.copyProperties(this, result);
        return result;
    }
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Dto extends DtoEntity {
        private String name;
        private String imgsrc;
        private Integer unread;

        public Person toBean() {
            Person result = new Person();
            BeanUtils.copyProperties(this, result);
            return result;
        }
    }
}
