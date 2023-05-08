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


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

//@FeignClient(value="nacos-service-baseser",fallback = FallbackService.class) // 指向服务提供者应用
@FeignClient(value = "nacos-service-baseser", fallbackFactory = FeignReqServiceFallBackFactory.class) //  //容错类中拿到具体的错误
public interface EchoService {

    @GetMapping("/echo/{message}")
    String echo(@PathVariable("message") String message);

    @RequestMapping("/break/test1/{type}")
    String test1(@PathVariable("type") Integer type);
}
