package chengweiou.universe.carina.service.room;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.Room;

import java.util.List;

public interface RoomService {
    void save(Room e) throws FailException;

    void delete(Room e) throws FailException;

    long update(Room e);

    Room findById(Room e);

    long count(SearchCondition searchCondition);
    List<Room> find(SearchCondition searchCondition);

    long count(SearchCondition searchCondition, Room sample);
    List<Room> find(SearchCondition searchCondition, Room sample);

    /**
     * 进入房间， 或创建solo房间
     * @param e
     * @return
     * @throws FailException
     */
    Room enter(Room e) throws FailException;

    //todo 加入leader
    /**
     * 创建新群聊,
     * @param e
     * @param name
     * @param imgsrc
     * @return
     */
    Room createGroup(Room e, String name, String imgsrc) throws FailException;
    // todo 群聊，创建，加入已存在

    /**
     *  存服务器: 清除 列表数字，减少 外面数字, 更新消息 !unread
     * !存服务器: 清除 列表数字，减少 外面数字，删除消息 !unread
     * @param person
     * @param room
     */
    void leaveRoom(Person person, Room room);
}
