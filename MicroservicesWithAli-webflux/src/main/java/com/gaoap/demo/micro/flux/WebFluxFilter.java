package com.gaoap.demo.micro.flux;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class WebFluxFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        ServerHttpRequest oldRequest = serverWebExchange.getRequest();
        ServerHttpRequest newRequest = oldRequest.mutate().headers(dd->oldRequest.getHeaders()).build();
        ServerWebExchange newServerWebExchange = serverWebExchange.mutate().request(newRequest).build();
        System.out.println("-------------------------------------");
        return webFilterChain.filter(newServerWebExchange);
    }
}
