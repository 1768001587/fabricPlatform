package one.hust.edu.cn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping(value = "/test/{id}")
    public String test(@PathVariable("id") int id){
        return "this is fabricTest id="+id;
    }
}
