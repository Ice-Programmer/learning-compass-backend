package com.ice.learningcompass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.ice.learningcompass.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class LearningCompassBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningCompassBackendApplication.class, args);
    }

}
