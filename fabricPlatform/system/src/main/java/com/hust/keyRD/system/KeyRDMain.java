package com.hust.keyRD.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.hust.keyRD.system.api.feign")
public class KeyRDMain {
    public static void main(String[] args) {
        SpringApplication.run(KeyRDMain.class,args);
    }
}
