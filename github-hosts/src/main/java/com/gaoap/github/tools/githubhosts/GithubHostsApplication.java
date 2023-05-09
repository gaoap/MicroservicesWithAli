package com.gaoap.github.tools.githubhosts;

import com.gaoap.github.tools.githubhosts.url.HostsUrlApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GithubHostsApplication {

    public static void main(String[] args) {

        SpringApplication.run(GithubHostsApplication.class, args);

    }

}
