package com.github.mtdp.job.api;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.SessionAwareMessageListener;

import com.alibaba.fastjson.JSON;
import com.github.mtdp.job.api.bean.JobDetailBean;
import com.github.mtdp.job.api.bean.JobExeLogBean;
import com.github.mtdp.util.QueueUtil;
import com.github.mtdp.util.SystemUtil;
import com.github.mtdp.util.UUIDUtil;

/**
 * 
 *
 * @Description tdp任务抽象客户端
 * @author wangguoqing
 * @date 2016年6月3日上午11:31:04
 *
 */
@SuppressWarnings("rawtypes")
public abstract class TdpJobAbstractClient implements SessionAwareMessageListener,ITdpJobClient{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private JmsTemplate jmsTemplate;
	/**任务日志保存队列**/
	private String jobLogQueueName = "job.log";
	
	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		TextMessage tmsg = (TextMessage)message;
		String jsonString = tmsg.getText();
		logger.info("开始执行任务jsonString={}",jsonString);
		JobDetailBean bean = JSON.parseObject(jsonString,JobDetailBean.class);
		
		JobExeLogBean log = new JobExeLogBean();
		log.setJobId(bean.getJobId());
		log.setJobKey(bean.getJobKey());
		log.setJobName(bean.getJobName());
		//赋值根据cron表达式解析后计划执行时间
		log.setPlanExeTime(bean.getLastExeTime());
		//获取本执行节点计算机名称及ip
		log.setNodeName(SystemUtil.getSystemLocalIp().toString());
		log.setStatus(JobConstantsCode.JOB_EXE_ING);
		log.setLogUUID(UUIDUtil.genUUID());
		log.setStartExeTime(new Date());
		//log.setSendAlarmMsg(bean.isAlarm());
		//发送消息至job-rt的队列落地任务执行日志
		QueueUtil.send2Queue(this.jmsTemplate, this.jobLogQueueName, log);
		
		//任务执行后的job日志对象
		JobExeLogBean jobExeAfterLog = new JobExeLogBean();
		jobExeAfterLog.setJobId(log.getJobId());
		jobExeAfterLog.setLogUUID(log.getLogUUID());
		jobExeAfterLog.setJobKey(log.getJobKey());
		try{
			boolean r = this.execute(bean);
			jobExeAfterLog.setEndExeTime(new Date());
			jobExeAfterLog.setStatus(JobConstantsCode.JOB_EXE_SUCCESS);
			if(!r){
				//执行失败
				jobExeAfterLog.setStatus(JobConstantsCode.JOB_EXE_FAIL);
			}
			logger.info("执行结果是r={}",r);
		}catch(Throwable e){
			logger.error("执行具体的任务异常",e);
			String ee = e.toString();
			int len = ee.length();
			if(len > 3000){
				ee = ee.substring(0, 3000);
			}
			jobExeAfterLog.setMemo("异常内容是:"+ee);
			jobExeAfterLog.setEndExeTime(new Date());
			jobExeAfterLog.setStatus(JobConstantsCode.JOB_EXE_FAIL);
		}
		//发送消息至job-rt的队列落地任务执行日志
		QueueUtil.send2Queue(this.jmsTemplate, this.jobLogQueueName, jobExeAfterLog);
	}
	
	/**
	 * 执行任务的方法
	 * @param message
	 * @return
	 */
    public abstract boolean execute(JobDetailBean message);
}
