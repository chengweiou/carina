package chengweiou.universe.carina.base.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import chengweiou.universe.blackhole.model.NotNullObj;
import lombok.Data;

@Data
public abstract class DtoEntity implements NotNullObj, Serializable {
    protected Long id;
    protected LocalDateTime createAt;
    protected LocalDateTime updateAt;

    public ServiceEntity toBean() {
        return null;
    }
}
