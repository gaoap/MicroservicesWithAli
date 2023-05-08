package com.gaoap.demo.micro.nacosdiscovery;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.balancer.NacosBalancer;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

public class LocalNacosLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Logger log = LoggerFactory.getLogger(com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancer.class);
    private final String serviceId;
    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final NacosDiscoveryProperties nacosDiscoveryProperties;

    public LocalNacosLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, NacosDiscoveryProperties nacosDiscoveryProperties) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
    }


    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = (ServiceInstanceListSupplier) this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        Object object = request.getContext();
        if (object instanceof RequestDataContext) {
            RequestDataContext headers = (RequestDataContext) request.getContext();
            String version = headers.getClientRequest().getHeaders().getFirst("version");
            return supplier.get().next().map(x -> this.getInstanceResponse(x, version));
        }

        return supplier.get().next().map(x -> this.getInstanceResponse(x, ""));
    }

    private boolean equalsOrNull(String localString, String ServerString) {
        if (StringUtils.isNotBlank(localString)) {
            return StringUtils.equals(localString, ServerString);
        } else {
            return true;
        }
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> serviceInstances, String version) {
        if (serviceInstances.isEmpty()) {
            log.warn("No servers available for service: " + this.serviceId);
            return new EmptyResponse();
        } else {
            try {
//                System.out.println("version: "+version);
                String clusterName = this.nacosDiscoveryProperties.getClusterName();
//                for (Map.Entry<String, String> entry : this.nacosDiscoveryProperties.getMetadata().entrySet()) {
//                    System.out.println(entry.getKey() + "," + entry.getValue());
//                }

                List<ServiceInstance> instancesToChoose = serviceInstances;
                if (StringUtils.isNotBlank(clusterName) || StringUtils.isNotBlank(version)) {
                    List<ServiceInstance> sameClusterInstances = serviceInstances.stream().filter(serviceInstance ->
                            this.equalsOrNull(clusterName, serviceInstance.getMetadata().get("nacos.cluster"))
                    ).filter(instance ->
                            this.equalsOrNull(version, instance.getMetadata().get("version"))
                    ).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                        //只有找到符合需求的服务实例才真正使用过滤后的实例。如果没有符合条件的实例，则使用全部实例
                        instancesToChoose = sameClusterInstances;
                    }
                } else {
                    log.warn("A cross-cluster call occurs，name = {}, clusterName = {}, instance = {}", new Object[]{this.serviceId, clusterName, serviceInstances});
                }
                ServiceInstance instance = NacosBalancer.getHostByRandomWeight3(instancesToChoose);
                return new DefaultResponse(instance);
            } catch (Exception var5) {
                log.warn("NacosLoadBalancer error", var5);
                return null;
            }
        }
    }
}
