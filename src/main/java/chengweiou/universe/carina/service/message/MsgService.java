package chengweiou.universe.carina.service.message;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.Room;

import java.util.List;

/**
 * 前端在线收到 ws
 * 对方:
 *  不在房间里: 增加 列表数字， 修改 列表文字， 增加 外面数字，
 *    在房间里:               修改 列表文字，            ，显示消息，ws发送 已读本消息
 */
public interface MsgService {
    /**
     *  存服务器: 增加 对方列表数字，修改 列表文字，增加 对方外面数字，保存 history， 保存 自己消息!unread，保存对方消息
     * !存服务器: 增加 对方列表数字，修改 列表文字，增加 对方外面数字，保存 history，!保存 自己消息       ，保存对方消息
     * @param e
     * @return
     * @throws FailException
     * @throws ProjException
     */
    List<History> send(History e) throws FailException, ProjException;

    /**
     *  存服务器: !unread指定消息，清0 列表数字，更新外面数字
     * !存服务器: 删除    指定消息，清0 列表数字，更新外面数字
     * @param searchCondition
     * @param person
     * @param room
     * @return
     */
    List<History> read(SearchCondition searchCondition, Person person, Room room);


    /**
     *  存服务器: 减少 列表数字，减少 外面数字, 更新消息 !unread
     * !存服务器: 减少 列表数字，减少 外面数字，删除消息
     * @param e
     */
    void readById(History e) throws FailException;

}
