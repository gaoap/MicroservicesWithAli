package com.gaoap.demo.micro.demos.nacosdiscoveryconsumer;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignReqServiceFallBackFactory2 implements FallbackFactory<EchoService> {

    @Override
    public EchoService create(Throwable throwable) {

        return new EchoService() {
            @Override
            public String echo(String message) {
                throwable.printStackTrace();
                return "nacos-service#echo服务降级返回,---FallbackService: " + message + throwable.getMessage();
            }

            @Override
            public String test1(Integer type) {
                throwable.printStackTrace();
                return "nacos-service#test1服务降级返回,---FallbackService: " + type + throwable.getMessage();
            }
//            @Override
//            public String getOrderInfo(String orderId) {
//                throwable.printStackTrace();
//                // 自定义返回异常
//                return "OrderFeignReqServiceFallBackFactory ";
//            }

        };
    }

}
