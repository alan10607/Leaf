package com.alan10607.leaf.schedule;

import com.alan10607.leaf.service.ViewService;
import com.alan10607.leaf.util.TimeUtil;
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
            log.info("Start schedule saveCountToDB");
            viewService.saveCountToDB();
            log.info("End schedule saveCountToDB");
        } catch (Exception e) {
            log.error("Schedule saveCountToDB fail");
            throw new JobExecutionException(e);
        }
    }

}
