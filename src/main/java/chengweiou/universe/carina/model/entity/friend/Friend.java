package chengweiou.universe.carina.model.entity.friend;

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
public class Friend extends ServiceEntity {
    private Person person;
    private Person target;

    public void fillNotRequire() {

    }

    public static final Friend NULL = new Friend.Null();
    public static class Null extends Friend implements NullObj {
        @Override public Person getPerson() { return Person.NULL; }
        @Override public Person getTarget() { return Person.NULL; }
    }
    public Dto toDto() {
        Dto result = new Dto();
        BeanUtils.copyProperties(this, result);
        if (person != null) result.setPersonId(person.getId());
        if (target != null) result.setTargetId(target.getId());
        return result;
    }
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Dto extends DtoEntity {
        @DtoKey
        private Long personId;
        @DtoKey
        private Long targetId;

        public Friend toBean() {
            Friend result = new Friend();
            BeanUtils.copyProperties(this, result);
            result.setPerson(Builder.set("id", personId).to(new Person()));
            result.setTarget(Builder.set("id", targetId).to(new Person()));
            return result;
        }
    }
}
