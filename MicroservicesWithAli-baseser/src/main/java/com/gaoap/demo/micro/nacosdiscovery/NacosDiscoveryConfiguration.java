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

import com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancer;
import com.gaoap.demo.micro.demos.nacosdiscoveryconsumer.RestTemplateInterceptor;
import jakarta.annotation.Resource;

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

import org.springframework.web.client.RestTemplate;


import java.util.List;


/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@EnableDiscoveryClient
@Configuration
@LoadBalancerClients(defaultConfiguration = {NacosDiscoveryConfiguration.class})
public class NacosDiscoveryConfiguration {
    @LoadBalanced
    @Bean
    public RestTemplate getRestTemplate(RestTemplateInterceptor restTemplateInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(restTemplateInterceptor));
        return restTemplate;
    }

    @Resource
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Bean
    ReactorLoadBalancer<ServiceInstance> nacosLoadBalancer(Environment environment,
                                                           LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new NacosLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name, nacosDiscoveryProperties);
    }

}
