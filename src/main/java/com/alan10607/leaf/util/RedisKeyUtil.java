
package com.alan10607.leaf.util;

import org.springframework.stereotype.Component;

@Component
public class RedisKeyUtil {

    private static final String LOCK_PREFIX = "lock";
    private static final String HASH_KEY_LEAF_PREFIX = "leaf";
    private static final String SCH_QUE = "scheduleQueue";

    /**
     * Get main leaf hash key for Redis
     * @param leafName
     * @return
     */
    public String getLeafKey(String leafName){
        return String.format("leaf:%s", leafName);
    }


    public String lock(String lockName){
        return new StringBuilder("lock-").append(lockName).toString();
    }


    public String systemLock(String functionName){
        return String.format("lock-system-%s", functionName);
    }

    public String saveLeafCountDB(){
        return "lock-system-saveLeafCountDB";
    }

    public String schQueue(){
        return "schedule-queue";
    }


    public int getExpireTime(int sec){
        return ((int) (Math.random() * 60)) + sec;//加上60秒
    }

}

