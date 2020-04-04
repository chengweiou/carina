package com.chengweiou.universe.carina.sdk;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "site")
@Component
@Data
public class SiteConfig {
    private String andromeda;
}
