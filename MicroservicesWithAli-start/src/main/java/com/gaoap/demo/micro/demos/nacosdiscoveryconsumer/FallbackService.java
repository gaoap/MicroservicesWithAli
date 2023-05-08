package com.gaoap.demo.micro.demos.nacosdiscoveryconsumer;

import org.springframework.stereotype.Component;

@Component
public class FallbackService implements EchoService {
    @Override
    public String echo(String message) {
        return "nacos-service#echo服务降级返回,---FallbackService: " + message;
    }

    @Override
    public String test1(Integer type) {
        return "nacos-service#test1服务降级返回,---FallbackService: " + type;
    }
}
