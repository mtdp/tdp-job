package com.github.mtdp.job.jms;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.alibaba.fastjson.JSON;
import com.github.mtdp.job.api.JobConstantsCode;
import com.github.mtdp.job.api.bean.JobExeLogBean;
import com.github.mtdp.job.dao.IJobDetailMapper;
import com.github.mtdp.job.dao.IJobExeLogMapper;
import com.github.mtdp.job.dao.domain.JobDetail;
import com.github.mtdp.job.dao.domain.JobExeLog;
import com.github.mtdp.util.BeanUtil;
/**
 * 
 *
 * @Description 监听任务执行的日志结果并保存到数据库
 * @author wangguoqing
 * @date 2016年7月30日下午4:09:25
 *
 */
@SuppressWarnings("rawtypes")
public class JobExeResultListener implements SessionAwareMessageListener{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IJobExeLogMapper jobExeLogMapper;
	
	@Autowired
	private IJobDetailMapper jobDetailMapper;
	
	@Autowired
	private JavaMailSenderImpl javaMailSenderImpl;

	@Override
	public void onMessage(Message message, Session session) throws JMSException {
		TextMessage tmsg = (TextMessage)message;
		String jsonString = tmsg.getText();
		logger.info("接收任务执行的日志jsonString:{}",jsonString);
		//json转换成obj
		JobExeLogBean bean = JSON.parseObject(jsonString,JobExeLogBean.class);
		if(bean.getEndExeTime() == null){
			//新增记录
			JobExeLog t = new JobExeLog();
			BeanUtil.copyProperties(bean, t);
			t.setCreateTime(new Date());
			t.setUpdateTime(new Date());
			this.jobExeLogMapper.add(t);
		}else{
			//更新记录  根据日志的uuid更新
			JobExeLog t = new JobExeLog();
			t.setLogUUID(bean.getLogUUID());
			t.setStatus(bean.getStatus());
			t.setEndExeTime(bean.getEndExeTime());
			t.setMemo(bean.getMemo());
			t.setUpdateTime(new Date());
			
			//如果执行失败 并且设置了需要发送报警信息,发送报警邮件
			Long jobId = bean.getJobId();
			JobDetail j = this.jobDetailMapper.get(jobId);
			try{
				if(bean.getStatus().intValue() == JobConstantsCode.JOB_EXE_FAIL && j.getIsAlarm().booleanValue()){
					logger.error("任务jobkey={}执行失败",bean.getJobKey());
					//TODO 可以考虑异步发送邮件
					this.sendMail(bean,j);
					t.setSendAlarmMsg(true);
				}
			}catch(Throwable e){
				logger.error("发送任务报警信息出错",e);
			}
			this.jobExeLogMapper.updateJobLogByUUID(t);
		}
	}
	
	/**
	 * 发送报警邮件
	 * @param bean
	 * @param job
	 */
	private void sendMail(JobExeLogBean bean,JobDetail job){
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(this.javaMailSenderImpl.getUsername());
		smm.setTo(job.getAlarmEmails().split(";"));
		smm.setSubject("【报警】任务执行失败");
		smm.setText("任务jobkey="+bean.getJobKey()+"执行失败,具体查看任务组件日志.");
		this.javaMailSenderImpl.send(smm);
		
	}
	
	public static void main(String[] args) {
		JavaMailSenderImpl mail = new JavaMailSenderImpl();
		String userName = "wangguoqing@aladingbank.com";
		String password = "china76!";
		String host = "smtp.qiye.163.com";
		mail.setUsername(userName);
		mail.setPassword(password);
		mail.setHost(host);
		mail.setDefaultEncoding("UTF-8");
		
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom(userName);
		smm.setTo("594566751@qq.com");
		smm.setSubject("这是一封测试");
		smm.setText("这里是测试内容");
		mail.send(smm);
	}
	
}