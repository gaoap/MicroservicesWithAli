package com.gaoap.demo.micro.demos.nacosdiscoveryconsumer;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignReqServiceFallBackFactory implements FallbackFactory<EchoService> {

    @Override
    public EchoService create(Throwable throwable) {

        return new EchoService() {
            @Override
            public String echo(String message) {
                throwable.printStackTrace();
                return "nacos-service#echo服务降级返回,---FallbackService: " + message + throwable.getClass();
            }

            @Override
            public String test1(Integer type) {
                throwable.printStackTrace();
                //com.alibaba.csp.sentinel.slots.block.degrade.DegradeException是真正的降级异常
                //feign.FeignException$InternalServerError是通信异常
                return "nacos-service#test1服务降级返回,---FallbackService: " + type + throwable.getClass();
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
