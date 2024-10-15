package com.ice.learningcompass;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan("com.ice.learningcompass.mapper")
public class LearningCompassBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearningCompassBackendApplication.class, args);
    }

}
