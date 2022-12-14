package com.alan10607.leaf.schedule;

import com.alan10607.leaf.service.ViewService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

@AllArgsConstructor
@Slf4j
public class RedisSchedule extends QuartzJobBean {

    private ViewService viewService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            viewService.saveCountToDB();
            log.info("Schedule saveCountToDB succeeded");
        } catch (Exception e) {
            log.error("Schedule saveCountToDB fail");
            throw new JobExecutionException(e);
        }
    }

}