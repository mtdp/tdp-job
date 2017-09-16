package com.github.mtdp.job.test;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


/**
 * 
 * trigger 测试
 * @Description TODO
 * @author wangguoqing
 * @date 2017年3月2日下午10:16:20
 *
 */
public class TriggerTest {
	public static void main(String[] args) {
		String cronExpression = "";
		TriggerKey triggerKey = new TriggerKey("trigger_job");
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		try {
			CronTrigger trigger = (CronTrigger)scheduler.getTrigger(triggerKey);
			//任务不存在,新建
			if(trigger == null){
				CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
				JobDetail jb = JobBuilder.newJob().withIdentity("").build();
				trigger = TriggerBuilder.newTrigger().withIdentity("trigger_job").withSchedule(schedBuilder).build();
				scheduler.scheduleJob(jb, trigger);
			}else{
				//存在任务,更新
				CronScheduleBuilder schedBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(schedBuilder).build();
				//重新修改配置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
