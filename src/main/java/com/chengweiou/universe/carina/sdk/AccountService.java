package com.chengweiou.universe.carina.sdk;


import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.LogUtil;
import com.chengweiou.universe.carina.base.converter.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class AccountService {
    @Autowired
    private SiteConfig siteConfig;
    public Rest save(Account e) {
        List<String> paramList = new ArrayList<>();
        paramList.add("username=" + e.getUsername());
        paramList.add("password=" + e.getPassword());
        if (e.getPerson() != null) paramList.add("person.id=" + e.getPerson().getId());
        String param = paramList.stream().collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(siteConfig.getAndromeda() + "/api/account")).timeout(Duration.ofMinutes(2))
                .method("POST", HttpRequest.BodyPublishers.ofString(param))
                .header("content-type", "application/x-www-form-urlencoded")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).get();
            return Rest.from(response, Long.class);
        } catch (InterruptedException | ExecutionException ex) {
            LogUtil.e("account save fail", ex);
            return Rest.fail(BasicRestCode.FAIL);
        }
    }

}
