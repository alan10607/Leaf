
package com.alan10607.leaf.util;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtil {
    public static final String SYSTEM_FEAFNAME = "system-leafName";//用來記錄有哪些leaf要更新

    /**
     * Get leaf's hashKey
     * @param leafName
     * @return
     */
    public String leafKey(String leafName){
        return String.format("leaf:%s", leafName);
    }

    /**
     * Get leaf's readWriteLock key
     * @param lockName
     * @return
     */
    public String lock(String lockName){
        return String.format("lock-leafRW-%s", lockName);
    }

    /**
     * Get system's lock key
     * @param functionName
     * @return
     */
    public String systemLock(String functionName){
        return String.format("lock-system-%s", functionName);
    }

    /**
     * Add random seconds to the expired time
     * @param sec
     * @return
     */
    public int getRandomExpire(int sec){
        return ((int) (Math.random() * 60)) + sec;//加上60秒
    }

}

