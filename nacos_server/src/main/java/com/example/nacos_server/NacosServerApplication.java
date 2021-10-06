package com.example.nacos_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.nacos_server")
@ServletComponentScan
@EnableScheduling
public class NacosServerApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.standalone", "true");

        SpringApplication.run(NacosServerApplication.class, args);
    }

}
