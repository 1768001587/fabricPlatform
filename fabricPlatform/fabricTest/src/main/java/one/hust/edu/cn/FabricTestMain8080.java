package one.hust.edu.cn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("one.hust.edu.cn.feign")
public class FabricTestMain8080 {
    public static void main(String[] args) {
        SpringApplication.run(FabricTestMain8080.class,args);
    }
}
