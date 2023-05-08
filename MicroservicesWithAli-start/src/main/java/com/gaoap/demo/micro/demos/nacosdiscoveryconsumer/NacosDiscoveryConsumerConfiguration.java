/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gaoap.demo.micro.demos.nacosdiscoveryconsumer;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;

//这个RequestInterceptor被扫描进入IOC容器，一旦扫描进入则全局生效
@Configuration
// 激活 @FeignClient
@EnableFeignClients(basePackages = {"com.gaoap.demo.micro.demos"})
public class NacosDiscoveryConsumerConfiguration  {
    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        // 跳过 content-length,防止报错Feign报错feign.RetryableException: too many bytes written executing
                        if (name.equals("content-length")) {
                            continue;
                        }
                        template.header(name, values);
                    }
                    System.out.println("version:"+request.getHeader("version"));
                }
            }
        };
    }

//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        System.out.println(request.getHeader("GrayRule"));
//        String value = request.getHeader("GrayRule");
//        //将灰度标记的请求头透传给下个服务
//        if (Boolean.TRUE.toString().equals(value)) {
//            //① 保存灰度发布的标记
//            requestTemplate.header("GrayRule", value);
//        }
//    }


}
