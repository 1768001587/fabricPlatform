package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.system.service.ChannelAuthorityService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan
@SpringBootConfiguration
@EnableAutoConfiguration
//@AutoConfigureMybatis
@Import(value = ChannelAuthorityServiceImpl.class)
//@MapperScan(value = "com.hust.keyRD.system.dao")
//@EnableFeignClients("com.hust.keyRD.system.api.feign")
@ComponentScan({"com.hust.keyRD.system", "com.hust.keyRD.commons"})
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