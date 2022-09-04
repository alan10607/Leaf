
package com.alan10607.leaf.util;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtil {
    public static final String SYSTEM_FEAFNAME = "system-leafName";

    /**
     * Get main leaf hash key for Redis
     * @param leafName
     * @return
     */
    public String leafKey(String leafName){
        return String.format("leaf:%s", leafName);
    }

    public String lock(String lockName){
        return String.format("lock-leafRW-%s", lockName);
    }

    public String systemLock(String functionName){
        return String.format("lock-system-%s", functionName);
    }

    public int getExpireTime(int sec){
        return ((int) (Math.random() * 60)) + sec;//加上60秒
    }

}

