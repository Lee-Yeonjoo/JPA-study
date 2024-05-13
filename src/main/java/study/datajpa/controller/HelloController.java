package study.datajpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test") //요청과 메소드를 매핑. 클래스와 메소드에서 사용가능
public class HelloController {

    @GetMapping("/hello") //GET요청과 메소드를 매핑. 메소드에서만 가능
    public String hello() {
        return "hello";
    }
}
