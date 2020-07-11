package chengweiou.universe.carina.base.jwt;

import chengweiou.universe.blackhole.exception.UnauthException;
import chengweiou.universe.blackhole.model.Builder;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.base.converter.Account;
import chengweiou.universe.carina.model.entity.person.Person;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtUtil {
    @Autowired
    private JwtConfig config;
    public String sign(Account account) {
        try {
            Date expiresAt = Date.from(LocalDateTime.now(ZoneId.of("UTC")).plus(config.getExpMinute(), ChronoUnit.DAYS).atZone(ZoneId.of("UTC")).toInstant());
            return JWT.create()
                    .withIssuer(config.getIssuer())
                    .withClaim("accountId", account.getId())
                    // andromeda 中是string
                    .withClaim("personId", account.getPerson().getId().toString())
                    .withClaim("extra", account.getExtra())
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC512(config.getSign()));
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            LogUtil.e("fail to sign jwt", exception);
            return "";
        }
    }

    public Account verify(String token) throws UnauthException {
        try {
            Algorithm algorithm = Algorithm.HMAC512(config.getSign());
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(config.getIssuer())
                    .build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
            return Builder
                    .set("id", jwt.getClaim("accountId").asLong())
                    .set("person", Builder.set("id", jwt.getClaim("personId").asString()).to(new Person()))
                    .set("extra", jwt.getClaim("extra").asString())
                    .to(new Account());
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            throw new UnauthException();
        }
    }
}
