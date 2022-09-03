
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
        return new StringBuilder(HASH_KEY_LEAF_PREFIX)
                .append(":")
                .append(leafName)
                .toString();
    }


    public String lock(String lockName){
        return new StringBuilder("lock-").append(lockName).toString();
    }


    public String getLeafCountDB(){
        return "lock-system-getLeafCountDB";
    }

    public String saveLeafCountDB(){
        return "lock-system-saveLeafCountDB";
    }

    public String schQueue(){
        return "schedule-queue";
    }

}

