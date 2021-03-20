package com.hust.keyRD.system.service.impl;

import com.hust.keyRD.commons.Dto.UserChannelAuthDto;
import com.hust.keyRD.commons.Dto.UserInnerDataDto;
import com.hust.keyRD.commons.vo.UserInnerDataVO;
import com.hust.keyRD.system.service.ChannelAuthorityService;
import com.hust.keyRD.system.service.DataService;
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
class ChannelAuthorityServiceImplTest {

    @Autowired
    private DataService dataService;
    
    @Test
    void getUserInnerDataListByUserIdAndChannelIdTest(){
        Integer userId = 131, channelId = 2;
        List<UserInnerDataVO> currentChannelData = dataService.getCurrentChannelData(userId);
        currentChannelData.forEach(System.out::println);
    }
    
//    @Autowired
//    private ChannelAuthorityService channelAuthorityService;
//
//    @Resource
//    private DataSource dataSource;
//
////    @Bean
////    public SqlSessionFactory sqlSessionFactory() throws Exception{
////        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
////        sessionFactoryBean.setDataSource(dataSource);
////        return sessionFactoryBean.getObject();
////    }
//
////    public void setChannelAuthorityService(ChannelAuthorityService channelAuthorityService) {
////        this.channelAuthorityService = channelAuthorityService;
////    }
//
//    @Test
//    void findUsersChannelAuthority() {
//        List<UserChannelAuthDto> usersChannelAuthority = channelAuthorityService.findUsersChannelAuthority(125, 1);
//        System.out.println(usersChannelAuthority);
//        usersChannelAuthority.forEach(System.out::println);
////        Map<Integer, List<UserChannelAuthDto>> collect = usersChannelAuthority.stream().collect(Collectors.groupingBy(UserChannelAuthDto::getAuthorityKey));
//
//    }
    
    
}