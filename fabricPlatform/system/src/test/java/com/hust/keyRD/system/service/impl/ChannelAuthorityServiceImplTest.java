package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.system.service.ChannelAuthorityService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ChannelAuthorityServiceImplTest {
    
    @Autowired
    private ChannelAuthorityService channelAuthorityService;
    
    @Resource
    private DataSource dataSource;
    
//    @Bean
//    public SqlSessionFactory sqlSessionFactory() throws Exception{
//        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        return sessionFactoryBean.getObject();
//    }

//    public void setChannelAuthorityService(ChannelAuthorityService channelAuthorityService) {
//        this.channelAuthorityService = channelAuthorityService;
//    }

    @Test
    void findUsersChannelAuthority() {
        List<UserChannelAuthDto> usersChannelAuthority = channelAuthorityService.findUsersChannelAuthority(125, 1);
        System.out.println(usersChannelAuthority);
        usersChannelAuthority.forEach(System.out::println);
//        Map<Integer, List<UserChannelAuthDto>> collect = usersChannelAuthority.stream().collect(Collectors.groupingBy(UserChannelAuthDto::getAuthorityKey));
        
    }
}