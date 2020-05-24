package chengweiou.universe.carina.base.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Component
@Data
public class JwtConfig {
    private String issuer;
    // token，过期了无法请求
    private Long expMinute;
    private String sign;
    // refresh token，用来获得新的token
    private Long refreshExpMinute;
}
