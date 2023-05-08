package com.gaoap.demo.micro.demos.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * 权限规则
 * 官方叫做黑白名单控制
 * <p>
 * 配置了白名单的资源只能允许配置的应用访问，拒绝其他应用的访问
 * 配置了黑名单的资源不允许配置的应用访问，允许其他应用访问
 * 一个资源只能配置一个白名单或黑名单
 * <p>
 * 黑白名单的配置是应用级别（origin）
 */

/**
 * 服务提供方解析来源，这里是从请求头中获取（也可以定义其他请求头）
 * RequestOriginParser 是 Sentinel 提供的接口
 */
@Component
public class CustomRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        String source = httpServletRequest.getHeader("source");
        System.out.println("请求来源：" + source);


        //1.解析请求参数,并返回参数值,然后将这个值应用在Sentinel的授权规则上
//        String origin = request.getParameter("origin");
//        return origin;
        //2.解析请求头,并返回参数值,然后将这个值应用在Sentinel的授权规则上
//        String token = request.getHeader("token");
//        System.out.println(token);
//        return token;
        //3.解析ip地址,并返回参数值,然后将这个值应用在Sentinel的授权规则上
//        String ip = request.getRemoteAddr();
//        System.out.println("ip地址:"+ip);
//        return ip;
        return source;
    }
}
