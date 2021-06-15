package chengweiou.universe.carina.base.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "jwt")
@Component
@Data
public class JwtConfig {
    private String issuer;
    // token，过期了无法请求
    private Long expMinute;
    // 2 选 1
    // sign 用于 hs 加密
    private String sign;
    // rsa 用于 rs 加密
    private String rsaPublicPath;
    private String rsaPrivatePath;
    // refresh token，用来获得新的token
    private Long refreshExpMinute;
}
