package com.gaoap.demo.micro.nacosdiscovery;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;
    @Resource
    WebConfig webConfig;
    public int getPort() {
        return this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
        this.webConfig.setServerPort(this.serverPort);
        log.info("Get WebServer port {}", serverPort);
    }
}

