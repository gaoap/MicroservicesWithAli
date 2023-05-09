package com.gaoap.github.tools.githubhosts.url;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Component
@Slf4j
public class GettingHosts {
    @Autowired
    private HostsUrlApi hHostsUrlApi;

    @Scheduled(cron = "0 */1 * * * ?")
    public void getting() {
        System.out.println("固定定时任务执行:--->" + new Date() + "，此任务为每五秒执行一次");
        List<String[]> list = hHostsUrlApi.list();
        List<String> hosts = new ArrayList<>();
        for (String[] host : list) {
            StringJoiner joiner = new StringJoiner("\t")
                    .add(host[0])
                    .add(host[1]);
            hosts.add(joiner.toString());


        }
        RWHosts.updateHost(hosts);
        System.out.println(RuntimeUtil.execForStr("ipconfig /flushdns"));
        log.info("更新完毕，请检查！");
    }

}
