package com.gaoap.demo.micro.demos.nacosdiscoveryconsumer;

public class FallbackService implements EchoService {
    @Override
    public String echo(String message) {
        return "nacos-service-baseser服务降级返回,---FallbackService: " + message;
    }
}
