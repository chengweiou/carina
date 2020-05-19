package chengweiou.universe.carina.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "proj")
@Component
@Data
public class ProjConfig {
    private Boolean serverHistory;
}
