package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.system.api.service.FabricService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients("com.hust.keyRD.system.api.feign")
class FabricServiceImplTest {

    @Autowired
    private FabricService fabricService;
    
    @Test
    void grantUserPermission2Add() {
        Boolean ans = fabricService.grantUserPermission2Add("channel1", "role1", "userB");
        System.out.println(ans);
    }

    @Test
    void updateForModifyFile() {
        Record record = fabricService.updateForModifyFile("233","user432","channel1","file1","feses3432frs");
        System.out.println(record);
        
    }
}