package com.andyadc.bms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@MapperScan({"com.andyadc.bms.auth.mapper"})
@SpringBootApplication
public class BmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BmsApplication.class, args);
    }
}
