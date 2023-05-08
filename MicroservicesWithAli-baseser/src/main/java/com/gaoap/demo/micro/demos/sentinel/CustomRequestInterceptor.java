package com.gaoap.demo.micro.demos.sentinel;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * 调用方定义传递请求头
 * 通过 feign 提供的拦截器，统一处理
 */

/**
 * 自定义 feign 请求的拦截器，请求发送前执行
 */
@Component
public class CustomRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("source", "cloud-sentinel-demo");
    }
}
