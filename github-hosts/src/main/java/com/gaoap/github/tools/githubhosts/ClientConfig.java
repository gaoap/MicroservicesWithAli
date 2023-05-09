package com.gaoap.github.tools.githubhosts;

import com.gaoap.github.tools.githubhosts.url.HostsUrlApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {
    @Bean
    HostsUrlApi demoApi() {
        WebClient client = WebClient.builder().baseUrl("https://raw.hellogithub.com/").build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
        return factory.createClient(HostsUrlApi.class);
    }
}
