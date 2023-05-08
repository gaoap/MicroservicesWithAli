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

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gaoap.demo.micro.permission.PublicApi;
import com.gaoap.demo.micro.permission.PublicScopeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class OpenFeignController {

    @Autowired
    private EchoService echoService;


    @GetMapping("/feign/echo/{message}")
    @SentinelResource(value = "feignEcho")
    public String feignEcho(@PathVariable String message) {
        return echoService.echo(message);

    }

    @GetMapping("/feign/break/test1/{message}")
    public String test1(@PathVariable int message) {
        System.out.println(message);
        return echoService.test1(message);

    }
}
