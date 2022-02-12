package chengweiou.universe.carina.model.entity.room;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import chengweiou.universe.blackhole.model.NullObj;
import chengweiou.universe.blackhole.model.entity.DtoEntity;
import chengweiou.universe.blackhole.model.entity.DtoKey;
import chengweiou.universe.blackhole.model.entity.ServiceEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Room extends ServiceEntity {
    private RoomType type;
    private List<Long> personIdList;

    public void fillNotRequire() {
        personIdList = personIdList != null ? personIdList : new ArrayList<>();
    }

    public static final Room NULL = new Room.Null();
    public static class Null extends Room implements NullObj {
    }
    public Dto toDto() {
        Dto result = new Dto();
        BeanUtils.copyProperties(this, result);
        if (personIdList != null) result.setPersonIdListString(personIdList.stream().distinct().sorted().map(String::valueOf).collect(Collectors.joining(",")));
        return result;
    }
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Dto extends DtoEntity {
        @DtoKey
        private RoomType type;
        @DtoKey
        private String personIdListString;
        public Room toBean() {
            Room result = new Room();
            BeanUtils.copyProperties(this, result);
            // 只查询id的情况下
            if (personIdListString != null) result.setPersonIdList(Pattern.compile(",").splitAsStream(personIdListString).map(Long::valueOf).toList());
            return result;
        }
    }
}
