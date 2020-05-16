package chengweiou.universe.carina.service.message;

import chengweiou.universe.blackhole.exception.FailException;
import chengweiou.universe.blackhole.exception.ProjException;
import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;
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

    @Override
    public void send(History e) throws FailException, ProjException {
        List<Long> personIdList = roomDio.findById(e.getRoom()).getPersonIdList();
        if (personIdList == null) throw new ProjException("发送消息: 房间不存在: " + e, ProjectRestCode.NOT_EXISTS);
        if (!personIdList.contains(e.getSender().getId())) throw new ProjException("发送消息: 用户不在房间内: " + e, BasicRestCode.UNAUTH);
        personIdList.stream().filter(id -> id != e.getSender().getId()).forEach(id -> {
            Person person = personDio.findById(Builder.set("id", id).to(new Person()));
            History history = Builder.set("person", person)
                    .set("room", e.getRoom()).set("sender", e.getSender()).set("type", e.getType()).set("v", e.getV())
                    .to(new History());
            try {
                historyDio.save(history);
                // 更新外面unread
                person.setUnread(person.getUnread() + 1);
                personTask.update(person);
                // 更新每个房间unread, last message
                List<PersonRoomRelate> relateList = personRoomRelateDio.find(new SearchCondition(), Builder.set("person", history.getPerson()).set("room", history.getRoom()).to(new PersonRoomRelate()));
                relateList.stream().forEach(relate -> {
                    relate.setUnread(relate.getUnread() + 1);
                    switch (history.getType()) {
                        case SYS: break;
                        case AUDIO: relate.setLastMessage("[audio]"); break;
                        case IMG: relate.setLastMessage("[img]"); break;
                        case MAP: relate.setLastMessage("[map]"); break;
                        default: relate.setLastMessage(e.getV());
                    }
                    personRoomRelateTask.update(relate);
                });
                e.setId(history.getId());
            } catch (FailException ex) {
                LogUtil.e("save history: " + e, ex);
            }
        });
    }
    // todo 读取消息后，保留还是删除，history 不删除要加入unread
    @Override
    public List<History> read(SearchCondition searchCondition, Person person, Room room) throws ExecutionException, InterruptedException {
        List<History> result = historyDio.find(searchCondition, Builder.set("person", person).set("room", room).to(new History()));
        Future<Long> deleteFuture = historyTask.delete(result);
        person = personDio.findById(person);
        person.setUnread(person.getUnread() - deleteFuture.get().intValue());
        personTask.update(person);
        List<PersonRoomRelate> relateList = personRoomRelateDio.find(new SearchCondition(), Builder.set("person", person).set("room", room).to(new PersonRoomRelate()));
        relateList.get(0).setUnread(0);
        personRoomRelateTask.update(relateList.get(0));
        return result;
    }
}
