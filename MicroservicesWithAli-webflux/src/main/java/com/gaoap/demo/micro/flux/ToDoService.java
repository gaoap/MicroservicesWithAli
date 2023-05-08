package com.gaoap.demo.micro.flux;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 服务间调用
 *
 * @Author xiarg
 * @CreateTime 2023/02/01  14:10
 */
@HttpExchange("/feign")
public interface ToDoService {
    //对于阻塞交换方法，我们通常应该返回ResponseEntity,而对于响应式方法，我们可以返回Mono/Flux类型。
    @GetExchange("/break/test1/{id}")
    String test(@PathVariable("id")  int id);

    //Reactive
    @GetExchange("/break/test1/{id}")
    Mono<String> testMono(@PathVariable("id")  int id);
    //Reactive
    @GetExchange("/break/test1/{id}")
    Flux<String> testFlux(@PathVariable("id")  int id);

//    @GetMapping("/feign/break/test1/{message}")
//    public String test1(@PathVariable int message) {
//        System.out.println(message);
//        return echoService.test1(message);
//
//    }
}
