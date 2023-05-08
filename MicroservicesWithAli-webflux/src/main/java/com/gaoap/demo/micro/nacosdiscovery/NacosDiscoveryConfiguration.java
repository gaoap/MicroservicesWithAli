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
package com.gaoap.demo.micro.nacosdiscovery;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.gaoap.demo.micro.flux.LocalWebClientAdapter;
import com.gaoap.demo.micro.flux.ToDoService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@EnableDiscoveryClient
@Configuration
@LoadBalancerClients(defaultConfiguration = {NacosDiscoveryConfiguration.class})
public class NacosDiscoveryConfiguration {
    @Bean
    @LoadBalanced
    public WebClient.Builder register() {
        return WebClient.builder();
    }

    @Bean
    public ToDoService remoteDemoLoadBalancer() {
        WebClient client = register().baseUrl("http://nacos-service")
                .filter((request, next) -> {
                    return Mono.deferContextual(contextView -> {
                        String value = contextView.get("version");
                        ClientRequest filtered = ClientRequest.from(request).headers(dd -> {
                                    dd.add("version", value);
                                    System.out.println("version:" + dd.getFirst("version"));
                                }
                        ).build();
                        return next.exchange(filtered);
                    });
                })
                .build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(LocalWebClientAdapter.forClient(client)).build();
        return factory.createClient(ToDoService.class);
    }

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Bean
    ReactorLoadBalancer<ServiceInstance> nacosLoadBalancer(Environment environment,
                                                           LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new LocalNacosLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name, nacosDiscoveryProperties);
    }
}
