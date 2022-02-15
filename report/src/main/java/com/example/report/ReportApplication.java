package com.example.report;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;


@SpringCloudApplication
@ComponentScan(basePackages = {"com.example.report","com.example.commonutil"})
@MapperScan(basePackages = "com.example.report.mapper")
@EnableFeignClients
public class ReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportApplication.class, args);
    }

}
