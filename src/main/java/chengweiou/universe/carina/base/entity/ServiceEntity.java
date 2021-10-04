package chengweiou.universe.carina.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import chengweiou.universe.blackhole.model.NotNullObj;
import chengweiou.universe.blackhole.model.NullObj;
import lombok.Data;

@Data
public abstract class ServiceEntity implements NotNullObj, Serializable {
    protected Long id;
    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;

    public void fillNotRequire() {
    }

    public void createAt() {
        createAt = LocalDateTime.now();
    }
    public void updateAt() {
        updateAt = LocalDateTime.now();
    }

    public static final ServiceEntity NULL = new Null();
    public static class Null extends ServiceEntity implements NullObj {
    }

    public DtoEntity toDto() { return null; }
}
