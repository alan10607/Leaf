package com.alan10607.leaf.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeUtil {

    public LocalDateTime now(){
        return  LocalDateTime.now();
    }
}
