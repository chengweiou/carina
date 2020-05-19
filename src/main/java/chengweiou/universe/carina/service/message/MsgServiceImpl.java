package chengweiou.universe.carina.service.message;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.config.ProjConfig;
import chengweiou.universe.carina.model.ProjectRestCode;
import chengweiou.universe.carina.model.SearchCondition;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.model.entity.room.PersonRoomRelate;
import chengweiou.universe.carina.model.entity.room.Room;
import chengweiou.universe.carina.service.person.PersonDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateDio;
import chengweiou.universe.carina.service.room.PersonRoomRelateTask;
import chengweiou.universe.carina.service.room.RoomDio;
import chengweiou.universe.carina.service.history.HistoryDio;
import chengweiou.universe.carina.service.history.HistoryTask;
import chengweiou.universe.carina.service.person.PersonTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class MsgServiceImpl implements MsgService {
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

    @Override
    public void send(History e) throws FailException, ProjException {
        List<Long> personIdList = roomDio.findById(e.getRoom()).getPersonIdList();
        if (personIdList == null) throw new ProjException("发送消息: 房间不存在: " + e, ProjectRestCode.NOT_EXISTS);
        if (!personIdList.contains(e.getSender().getId())) throw new ProjException("发送消息: 用户不在房间内: " + e, BasicRestCode.UNAUTH);
        sendSelf(Builder
                .set("person", e.getSender()).set("room", e.getRoom()).set("sender", e.getSender())
                .set("type", e.getType()).set("v", e.getV()).set("unread", false)
                .to(new History()));
        for (Long personId : personIdList) {
            if (personId == e.getSender().getId()) continue;
            sendOther(Builder
                    .set("person", Builder.set("id", personId).to(new Person())).set("room", e.getRoom()).set("sender", e.getSender())
                    .set("type", e.getType()).set("v", e.getV())
                    .to(new History()));
        }

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

    @Override
    public List<History> read(SearchCondition searchCondition, Person person, Room room) {
        // 读取本房间 未读
        // todo 如果服务端不保存，记录应从旧的开始读取，客户端反复调用，一直读取到没有
        List<History> result = historyDio.find(searchCondition, Builder.set("person", person).set("room", room).to(new History()));
        // // 服务端保存，则 本房间消息更新为 已读。否则 删除已读取记录
        if (config.getServerHistory()) {
            // 服务端保存，则 本房间消息更新为 已读
            historyDio.updateUnreadByRoomAndPerson(Builder.set("person", person).set("room", room).set("unread", false).to(new History()));
        } else {
            // 客户端保存，则 本房间读取者 删除
            historyTask.delete(result);
        }
        // 更新数字为 剩余未读量
        person = personDio.findById(person);
        long personUnread = historyDio.count(searchCondition, Builder.set("person", person).set("unread", true).to(new History()));
        person.setUnread(Math.toIntExact(personUnread));
        personTask.update(person);
        // 更新本房间未读量 0
        PersonRoomRelate relate = personRoomRelateDio.findByKey(Builder.set("person", person).set("room", room).to(new PersonRoomRelate()));
        relate.setUnread(0);
        personRoomRelateTask.update(relate);
        return result;
    }
}
