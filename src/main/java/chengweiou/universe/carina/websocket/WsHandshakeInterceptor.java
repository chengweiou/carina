package chengweiou.universe.carina.websocket;

import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.base.jwt.JwtUtil;
import chengweiou.universe.carina.service.person.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WsHandshakeInterceptor implements HandshakeInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PersonService personService;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String path = request.getURI().getPath();
        String token = path.substring(path.lastIndexOf("/") + 1);
        attributes.put("token", token);
        try {
            Account account = jwtUtil.verify(token);
            // tip: 推送消息的时候要名字
            attributes.put("user", personService.findById(account.getPerson()));
        } catch (UnauthException ex) {
            LogUtil.i("ws handshake fail: token: " + token);
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
