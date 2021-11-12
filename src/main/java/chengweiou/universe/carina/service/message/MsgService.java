package chengweiou.universe.carina.service.message;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.carina.config.ProjConfig;
import chengweiou.universe.carina.model.ProjectRestCode;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.service.history.HistoryDio;
import chengweiou.universe.carina.service.history.HistoryTask;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.person.PersonTask;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateTask;
import chengweiou.universe.carina.service.room.RoomDio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MsgService {
    @Autowired
    private HistoryDio historyDio;
    @Autowired
    private HistoryTask historyTask;
    @Autowired
    private RoomDio roomDio;
    @Autowired
    private PersonDio personDio;
    @Autowired
    private PersonTask personTask;
    @Autowired
    private PersonRoomRelateDio personRoomRelateDio;
    @Autowired
    private PersonRoomRelateTask personRoomRelateTask;
    @Autowired
    private ProjConfig config;

    /**
     *  存服务器: 增加 对方列表数字，修改 列表文字，增加 对方外面数字，保存 history， 保存 自己消息!unread，保存对方消息
     * !存服务器: 增加 对方列表数字，修改 列表文字，增加 对方外面数字，保存 history，!保存 自己消息       ，保存对方消息
     * @param e
     * @return
     * @throws FailException
     * @throws ProjException
     */
    public List<History> send(History e) throws FailException, ProjException {
        List<Long> personIdList = roomDio.findById(e.getRoom()).getPersonIdList();
        if (personIdList == null) throw new ProjException("发送消息: 房间不存在: " + e, ProjectRestCode.NOT_EXISTS);
        if (!personIdList.contains(e.getSender().getId())) throw new ProjException("发送消息: 用户不在房间内: " + e, BasicRestCode.UNAUTH);
        List<History> result = new ArrayList<>();
        History selfHistory = Builder
                .set("person", e.getSender()).set("room", e.getRoom()).set("sender", e.getSender())
                .set("type", e.getType()).set("v", e.getV()).set("unread", false)
                .to(new History());
        result.add(selfHistory);
        sendSelf(selfHistory);
        for (Long personId : personIdList) {
            if (personId == e.getSender().getId()) continue;
            History otherHistory = Builder
                    .set("person", Builder.set("id", personId).to(new Person())).set("room", e.getRoom()).set("sender", e.getSender())
                    .set("type", e.getType()).set("v", e.getV())
                    .to(new History());
            sendOther(otherHistory);
            result.add(otherHistory);
        }
        return result;
    }

    private void sendSelf(History e) throws FailException {
        // if 服务器存储 保存这条自己发的消息
        if (config.getServerHistory()) {
            historyDio.save(e);
        }
        // 如果不保存，需要填充数据 type
        e.fillNotRequire();
        PersonRoomRelate relate = personRoomRelateDio.findByKey(Builder.set("room", e.getRoom()).set("person", e.getPerson()).to(new PersonRoomRelate()));
        relate.setLastMessage(getMessageByType(e));
        relate.setLastMessageAt(Instant.now());
        personRoomRelateTask.update(relate);
    }

    private void sendOther(History e) throws FailException {
        Person person = personDio.findById(e.getPerson());
        historyDio.save(e);
        person.setUnread(person.getUnread() + 1);
        personTask.update(person);

        PersonRoomRelate relate = personRoomRelateDio.findByKey(Builder.set("room", e.getRoom()).set("person", e.getPerson()).to(new PersonRoomRelate()));
        relate.setUnread(relate.getUnread() + 1);
        relate.setLastMessage(getMessageByType(e));
        relate.setLastMessageAt(Instant.now());
        personRoomRelateTask.update(relate);
    }

    private String getMessageByType(History history) {
        switch (history.getType()) {
            case AUDIO: return "[audio]";
            case IMG: return "[img]";
            case MAP: return "[map]";
            case SYS:
            default: return history.getV();
        }
    }

    /**
     *  存服务器: 减少 列表数字，减少 外面数字, 更新消息 !unread
     * !存服务器: 减少 列表数字，减少 外面数字，删除消息
     * @param e
     */
    public void readById(History e) throws FailException {
        History indb = historyDio.findById(e);
        if (e.getPerson().getId() != indb.getPerson().getId()) throw new FailException("尝试清理不属于自己的消息" + e);
        if ( ! indb.getUnread()) return;
        if (config.getServerHistory()) {
            // 服务端保存，则 本消息 更新已读
            historyDio.update(Builder.set("id", indb.getId()).set("unread", false).to(new History()));
        } else {
            // 客户端保存，则 本消息 删除
            historyDio.delete(indb);
        }
        PersonRoomRelate relate = personRoomRelateDio.findByKey(Builder.set("person", indb.getPerson()).set("room", indb.getRoom()).to(new PersonRoomRelate()));
        personRoomRelateTask.update(Builder.set("id", relate.getId()).set("unread", relate.getUnread() -1).to(new PersonRoomRelate()));

        // 更新数字为 剩余未读量
        Person person = personDio.findById(e.getPerson());
        long personUnread = historyDio.count(new SearchCondition(), Builder.set("person", person).set("unread", true).to(new History()));
        person.setUnread(Math.toIntExact(personUnread));
        personTask.update(person);
    }

    /**
     *  存服务器: !unread指定消息，清0 列表数字，更新外面数字
     * !存服务器: 删除    指定消息，清0 列表数字，更新外面数字
     * @param searchCondition
     * @param person
     * @param room
     * @return
     */
    public List<History> read(SearchCondition searchCondition, Person person, Room room) {
        // 读取本房间 未读
        List<History> result = historyDio.find(Builder.set("limit", 0).to(new SearchCondition()), Builder.set("person", person).set("room", room).set("unread", true).to(new History()));
        // // 服务端保存，则 本房间消息更新为 已读。否则 删除已读取记录
        if (config.getServerHistory()) {
            // 服务端保存，则 本房间消息更新为 已读
            historyDio.updateUnreadByRoomAndPerson(Builder.set("person", person).set("room", room).set("unread", false).to(new History()));
        } else {
            // 客户端保存，则 本房间读取者 删除
            historyTask.delete(result);
        }
        // 更新本房间未读量 0
        PersonRoomRelate relate = personRoomRelateDio.findByKey(Builder.set("person", person).set("room", room).to(new PersonRoomRelate()));
        relate.setUnread(0);
        personRoomRelateTask.update(relate);
        // 更新数字为 剩余未读量
        person = personDio.findById(person);
        long personUnread = historyDio.count(searchCondition, Builder.set("person", person).set("unread", true).to(new History()));
        person.setUnread(Math.toIntExact(personUnread));
        personTask.update(person);
        return result;
    }
}
