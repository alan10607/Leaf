package com.alan10607.leaf.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
@AllArgsConstructor
@Slf4j
public class TestController {

    @GetMapping("/test")
    public String confirm(){
        return "Hello Leaf";
    }

}
