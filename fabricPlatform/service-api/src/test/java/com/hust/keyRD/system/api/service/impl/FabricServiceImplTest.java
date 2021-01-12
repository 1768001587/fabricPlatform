package com.hust.keyRD.system.api.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.keyRD.commons.entities.Record;
import com.hust.keyRD.commons.exception.fabric.FabricException;
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

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
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
    void traceBackward() {
    }

    @Test
    void testTraceBackward() {
        String fileId = "file1";
        String txId = "28df397b16ec36df92a23cd179ff5a10808bae543c776e3f6304c9db368724ba";
        Record record = fabricService.traceBackward(fileId);
        System.out.println(record);
//        String response = "{\"next_tx\": \"28df397b16ec36df92a23cd179ff5a10808bae543c776e3f6304c9db368724ba\", \"tx_info\":\n" +
//                "\"{\\\"data_id\\\":\\\"file1\\\",\\\"dst_chain\\\":\\\"channel1\\\",\\\"hash_data\\\":\\\"456\\\",\\\"last_tx_id\\\":\\\"cbcf5226f0aaf8754859b6b22fa8a14116bc84191ab8d720011abaa6ea846a47\\\",\\\"src_chain\\\":\\\"channel2\\\",\\\"this_tx_id\\\":\\\"28df397b16ec36df92a23cd179ff5a10808bae543c776e3f6304c9db368724ba\\\",\\\"type_tx\\\":\\\"delete\\\",\\\"user\\\":\\\"userC\\\"}\"}";
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            JsonNode root = mapper.readTree(response);
//            JsonNode txInfo = root.path("tx_info");
//            mapper.readValue(root.path("tx_info").toString(), Record.class);
//        } catch (IOException e) {
////            log.error("溯源失败,fileId:{},txId:{}, response:{}", fileId, txId, response);
//            throw new FabricException("溯源失败: fileId:" + fileId + ", txId: " + txId);
//        }
    }
}