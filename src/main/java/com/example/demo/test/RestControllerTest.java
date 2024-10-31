package com.example.demo.test;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class RestControllerTest {
    @GetMapping("/")
    public String root() {
        return "root";
    }
}
