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
package com.gaoap.demo.micro.demos.nacosdiscoveryprovider;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.gaoap.demo.micro.nacosdiscovery.WebConfig;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class EchoServiceController {
    @Resource
    WebConfig webConfig;
    @GetMapping("/echo/{message}")
    public String echo(@PathVariable String message) {
        return "[ECHO] : " + message +" [PORT] : "+ webConfig.getServerPort();
    }
    //热点参数：注意：仅支持 QPS 限流模式，HTTP 接口不能使用默认的资源名会不生效，需要用 @SentinelResource 定义资源
    @GetMapping("/echoIP/{ip}")
    @SentinelResource("echoIp")
    public String echoIp(@PathVariable String ip) {
        return "[ip] : " + ip +" [PORT] : "+ webConfig.getServerPort();
    }
    //注意：仅支持 QPS 限流模式，HTTP 接口不能使用默认的资源名会不生效，需要用 @SentinelResource 定义资源
    @SentinelResource("echoIpName")
    @GetMapping("/echoIP/{ip}/{name}")
    public String echoIpName(@PathVariable String ip,@PathVariable String name) {
        return "[ip] : " + ip+" [name] : " + name + " [PORT] : "+ webConfig.getServerPort();
    }
}
