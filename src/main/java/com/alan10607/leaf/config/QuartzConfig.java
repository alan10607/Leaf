package com.alan10607.leaf.config;

import com.alan10607.leaf.schedule.RedisSchedule;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    /**
     * 綁定具體的工作
     * @return
     */
    @Bean
    public JobDetail redisJobDetail(){
        return JobBuilder.newJob(RedisSchedule.class).storeDurably().build();//storeDurably表示持久化任務
    }

    /**
     * 綁定對應之工作明細
     * @return
     */
    @Bean
    public Trigger redisTrigger(){
        ScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0/15 * * * ?"); //從0分開始, 每15分鐘執行一次
        return TriggerBuilder.newTrigger().forJob(redisJobDetail()).withSchedule(scheduleBuilder).build();
    }

}
