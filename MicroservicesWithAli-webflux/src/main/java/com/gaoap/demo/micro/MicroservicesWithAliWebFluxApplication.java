package com.gaoap.demo.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//********************************** 第一种，项目不需要连接数据库，启动报错 ******************************************
//
//        解决方法如下：
//
//        只要在将@SpringBootApplication修改为@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})就可以启动的时候不需要连接数据库。
//
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class MicroservicesWithAliWebFluxApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicesWithAliWebFluxApplication.class, args);
    }

}
