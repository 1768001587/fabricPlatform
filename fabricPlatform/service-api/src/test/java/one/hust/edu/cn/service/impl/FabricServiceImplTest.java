package one.hust.edu.cn.service.impl;

import one.hust.edu.cn.entities.Record;
import one.hust.edu.cn.service.FabricService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients("one.hust.edu.cn.feign")
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