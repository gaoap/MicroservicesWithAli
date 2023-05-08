package com.gaoap.image.nativeimage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//传统的Spring Boot3单元测试技术仍然可以使用。需要注意的是 Spring Native不支持JUnit4，需要使用JUnit5。
@SpringBootApplication
@Slf4j
public class NativeImageApplication {
    public static void main(String[] args) {
        System.out.println("程序启动了！");
        log.info("程序启动了！.......");
        SpringApplication.run(NativeImageApplication.class, args);
        log.info("程序持续执行中......");
    }
}
