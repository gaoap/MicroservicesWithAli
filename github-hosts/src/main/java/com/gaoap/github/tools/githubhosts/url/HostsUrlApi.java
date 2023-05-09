package com.gaoap.github.tools.githubhosts.url;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange
public interface HostsUrlApi {
    @GetExchange("hosts.json")
    List<String[]> list();
    @GetExchange("hosts.json")
    String listAll();
}
