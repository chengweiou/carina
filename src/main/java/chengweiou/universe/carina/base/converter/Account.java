package chengweiou.universe.carina.base.converter;


import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.carina.model.entity.person.Person;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class Account implements NotNullObj, Serializable {
    private Person person;
    private String username;
    @JsonIgnore
    private String password;
    private String extra;
    public static final Account NULL = new Account.Null();
    public static class Null extends Account implements NullObj {
        @Override
        public Person getPerson() { return Person.NULL; }
    }

}
