package com.gaoap.demo.micro.nacosdiscovery;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * 自定义的限流处理
 */
//@Component
public class MyBlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
        String message = "{\"code\":\"-1\", \"message\":\"nacos-service-baseser:自定义的限流返回值~~~~\"}";
        httpServletResponse.getWriter().write(message);
    }
}
