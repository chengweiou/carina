package chengweiou.universe.carina.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.param.Valid;
import chengweiou.universe.blackhole.util.GsonUtil;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.manager.PushManager;
import chengweiou.universe.carina.model.entity.history.History;
import chengweiou.universe.carina.model.entity.person.Person;
import chengweiou.universe.carina.sdk.push.Push;
import chengweiou.universe.carina.service.message.MsgService;
import chengweiou.universe.carina.service.person.PersonDio;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;

@Service
public class TextHandler extends TextWebSocketHandler {
    @Autowired
    private MsgService msgService;
    @Autowired
    private PushManager pushManager;
    @Autowired
    private PersonDio personDio;

    // todo 如何定时判断前端还在连接着？否则删除节约内存
    // 定时检查记录时间，如果收到ping，刷新时间？
    private static final ConcurrentMap<Long, WebSocketSession> PC_SESSION_MAP = new ConcurrentHashMap<>();
    private static final ConcurrentMap<Long, WebSocketSession> PHONE_SESSION_MAP = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Person person = (Person) session.getAttributes().get("user");
        switch (getDeviceType(session)) {
            case COMPUTER -> {
                // 切换设备
                if (PC_SESSION_MAP.containsKey(person.getId())) PC_SESSION_MAP.get(person.getId()).close(new CloseStatus(4001, "多点登陆, 被踢下线"));
                PC_SESSION_MAP.put(person.getId(), session);
            }
            default -> {
                if (PHONE_SESSION_MAP.containsKey(person.getId())) PHONE_SESSION_MAP.get(person.getId()).close(new CloseStatus(4001, "多点登陆, 被踢下线"));
                PHONE_SESSION_MAP.put(person.getId(), session);
            }

        }
        super.afterConnectionEstablished(session);
    }

    // todo exception projexception...
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        if (message.getPayloadLength() == 0) {
            session.sendMessage(new TextMessage(""));
            return;
        }
        if (message.getPayload().equals("close")) {
            session.close();
            return;
        }
        History history = GsonUtil.create().fromJson(message.getPayload(), History.class);
        Valid.check("history.room", history.getRoom()).isNotNull();
        Valid.check("history.room.id", history.getRoom().getId()).is().positive();
        Valid.check("history.v", history.getV()).is().lengthIn(100);
        history.setSender((Person) session.getAttributes().get("user"));

        List<History> list = msgService.send(history);
        for (History e : list) {
            e.setExtra(history.getExtra());
            e.updateAt();
            try {
                String rest = GsonUtil.create().toJson(Rest.ok(e));
                WebSocketSession targetSession = PC_SESSION_MAP.get(e.getPerson().getId());
                if (targetSession != null) targetSession.sendMessage(new TextMessage(rest));
                targetSession = PHONE_SESSION_MAP.get(e.getPerson().getId());
                if (targetSession != null) targetSession.sendMessage(new TextMessage(rest));
                Person person = personDio.findById(e.getPerson());
                pushManager.pushAsync(Builder.set("person", e.getPerson()).set("name", e.getSender().getName()).set("content", e.getV()).set("notifyType", "chat").set("num", person.getUnread()).to(new Push()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Person person = (Person) session.getAttributes().get("user");
        switch (getDeviceType(session)) {
            case COMPUTER -> PC_SESSION_MAP.remove(person.getId(), session);
            default -> PHONE_SESSION_MAP.remove(person.getId(), session);
        }
        super.afterConnectionClosed(session, status);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        LogUtil.i("handlePongMessage");

        super.handlePongMessage(session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LogUtil.i("handleTransportError");
        super.handleTransportError(session, exception);
    }

    private DeviceType getDeviceType(WebSocketSession session) {
        String userAgentString = session.getHandshakeHeaders().get("User-Agent").toString(); // todo 用于判断手机 or pc
        return UserAgent.parseUserAgentString(userAgentString).getOperatingSystem().getDeviceType();
    }
}
