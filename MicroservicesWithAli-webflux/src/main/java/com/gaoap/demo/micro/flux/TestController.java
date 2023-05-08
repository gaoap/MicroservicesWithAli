package com.gaoap.demo.micro.flux;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
public class TestController {

    @Autowired
    private ToDoService toDoService;
    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/webflux/test1/{message}")
    public String test(@PathVariable Integer message) {
        String test = toDoService.test(message);
        return "name : " + test;
    }

    @GetMapping("/webflux/testMono/{message}")
    public String testMono(@PathVariable Integer message) {
        System.out.println(message);
        String test = toDoService.testMono(message).block();
//        Disposable test = toDoService.testMono(message).subscribe(data->System.out.println("测试："+data));
        System.out.println(test);
        return "name : " + test;
    }

    @GetMapping("/webflux2/test1/{message}")
    public String test2(@PathVariable Integer message) {
        System.out.println(message);
        String result = webClientBuilder.build()
                .get()
                //http://file 会被nacos等价替换成file的ip+端口号
                .uri("http://nacos-service/feign/break/test1/" + message)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return "name : " + result;
    }

}
