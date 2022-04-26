package com.design.onlineorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.design.onlineorder.mapper")
public class OnlineOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineOrderApplication.class, args);
    }

}
