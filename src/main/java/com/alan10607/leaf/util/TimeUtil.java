package com.alan10607.leaf.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TimeUtil {
    private final ZoneId utcPlus8 = ZoneId.of("UTC+8");

    public LocalDateTime now(){
        return LocalDateTime.now(utcPlus8);
    }

}
