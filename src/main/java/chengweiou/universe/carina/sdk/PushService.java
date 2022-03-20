package chengweiou.universe.carina.sdk;


import chengweiou.universe.blackhole.model.BasicRestCode;
import chengweiou.universe.blackhole.model.Rest;
import chengweiou.universe.blackhole.util.LogUtil;
import chengweiou.universe.carina.model.Push;

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
public class PushService {
    @Autowired
    private SiteConfig siteConfig;
    public Rest<Long> push(Push e) {
        if (siteConfig.getLeob().isEmpty()) {
            LogUtil.i("did NOT set up push server: leob");
            return Rest.fail(BasicRestCode.FAIL);
        }
        List<String> paramList = new ArrayList<>();
        paramList.add("person.id=" + e.getPerson().getId());
        paramList.add("name=" + e.getName());
        paramList.add("content=" + e.getContent());
        paramList.add("notifyType=" + e.getNotifyType());
        paramList.add("num=" + e.getNum());
        if (e.getTopic()!=null) paramList.add("topic=" + e.getTopic());
        String param = paramList.stream().collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(siteConfig.getLeob() + "/mg/push")).timeout(Duration.ofMinutes(2))
                .method("POST", HttpRequest.BodyPublishers.ofString(param))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("inServer", "true")
                .build();
        HttpClient client = HttpClient.newHttpClient();
        try {
            String response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body).get();
            return Rest.from(response, Long.class);
        } catch (InterruptedException | ExecutionException ex) {
            LogUtil.e("push notification fail", ex);
            return Rest.fail(BasicRestCode.FAIL);
        }
    }

}
