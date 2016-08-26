package com.github.mtdp.job.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.github.mtdp.job.api.bean.JobDetailBean;
import com.github.mtdp.job.dao.IJobDetailMapper;
import com.github.mtdp.job.dao.domain.JobDetail;
import com.github.mtdp.job.service.IJobContainer;
import com.github.mtdp.job.util.CronExpressUtil;
import com.github.mtdp.util.BeanUtil;
import com.github.mtdp.util.DateUtil;
import com.github.mtdp.util.QueueUtil;
/**
 * 
 *
 * @Description 任务执行容器实现
 * @author wangguoqing
 * @date 2016年7月31日上午8:40:09
 *
 */
@Service("job.service.impl.JobContainerImpl")
public class JobContainerImpl implements IJobContainer {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**根据心跳控制同一时间只有一个job服务节点执行 true表示次节点执行,false标识此节点不执行,默认false**/
	private volatile boolean isRun = false;
	/**查询数据库需要执行任务的间隔时间,默认30s**/
	private volatile long sleepTime = 30 * 1000;
	/**过滤最大x毫秒内需要执行的任务,默认1分钟=60s=60000毫秒**/
	private volatile long maxExeJobTime = 60 * 1000;
	
	@Autowired
	private IJobDetailMapper jobDetailMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	public void process() {
		//1.查询数据库获取所所有启用的任务 每30s(配置参数)执行一次查询数据库
		//2.循环过滤最近1分钟(并且最近执行任务时间大于当前时间)需要执行的任务
		//3.将最近执行任务的时间更新至数据库
		logger.info("开始循环执行任务处理isRun={}",this.isRun);
		while(isRun){
			List<JobDetail> jobs = this.jobDetailMapper.getNeedExeJobDetails();
			logger.info("需要处理的任务数量cnt={}",jobs.size());
			for(JobDetail j : jobs){
				Date d = new Date();
				//任务下次执行的时间
				Date nextExeTime = CronExpressUtil.getLastTime(j.getCronExpress(), d);
				long lagVal = DateUtil.calculateTwoTimeLag(nextExeTime, d);
				if(lagVal > 0 && lagVal < this.maxExeJobTime){
					j.setLastExeTime(nextExeTime);
					this.exeJob(j);
				}else{
					//当前时间={},key={},最近一次执行时间={}
					logger.info("当前时间={},任务key={}此次不需要执行,最近一次执行时间={}",DateUtil.getCurrentTime2(),j.getJobKey(),DateUtil.formatDefault2(j.getLastExeTime()));
				}
			}
			try {
				logger.info("获取数据线程开始休眠sleepTime={}ms",sleepTime);
				Thread.sleep(this.sleepTime);
				logger.info("获取数据线程休眠结束sleepTime={}ms",sleepTime);
			} catch (InterruptedException e) {
				logger.error("查询数据库线程休息被打断sleepTime={}ms",this.sleepTime);
			}
		}
	}
	
	@Override
	public void exeJob(JobDetail job) {
		JobDetailBean bean = new JobDetailBean();
		BeanUtil.copyProperties(job, bean);
		JobDetail t = new JobDetail();
		t.setJobId(job.getJobId());
		t.setLastExeTime(job.getLastExeTime());
		t.setUpdateTime(new Date());
		int c = this.jobDetailMapper.update(t);
		if(c == 1){
			//消息通知相应的app执行任务, 如果消息发送失败更新任务最近一次执行时间回滚
			QueueUtil.send2Queue(this.jmsTemplate, job.getExeJobQueueName(), bean);
		}else{
			logger.error("任务key={},更新数据库失败,取消通知app执行任务",job.getJobKey());
		}
	}
	
	@Override
	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	
	@Override
	public boolean getRun() {
		return this.isRun;
	}
}