package com.gaoap.demo.micro.demos.nacosdiscoveryconsumer;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(value="nacos-service-baseser",fallback = FallbackService.class) // 指向服务提供者应用
//public interface BreakService {
//    @GetMapping("/break/test1/{type}")
//    //@SentinelResource(value = "BreakService#test1")
//    String test1(@PathVariable ("type") Integer type);
//}
