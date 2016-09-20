package com.github.mtdp.job.api.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 *
 * @Description 任务描述bean
 * @author wangguoqing
 * @date 2016年6月3日下午12:36:35
 *
 */
public class JobDetailBean implements Serializable{
	
	private static final long serialVersionUID = 8961381119568322761L;
	
	private Long jobId;
	/**任务key,全局唯一**/
	private String jobKey;
	/**任务名称**/
	private String jobName;
	/**任务所属应用名称**/
	private String appName;
	/**执行任务队列名称**/
	private String exeJobQueueName;
	/**任务负责人手机号码**/
	private String ownerMobile;
	/**任务执行需要的数据json串**/
	private String jobDataJson;
	/**执行此任务的cron表达式**/
	private String cronExpress;
	/**任务接受的最大延迟执行时间,单位秒;默认值=0,不延迟;值需大于0**/
	private Integer maxDelayTime;
	/**执行失败是否报警**/
	private Boolean isAlarm;
	/**接受报警邮箱,多个邮箱用;隔开**/
	private String alarmEmails;
	/**5=禁用,10=启用**/
	private Integer status;
	/**最近一次需要执行任务的时间**/
	private Date lastExeTime;
	private Date createTime;
	private Date updateTime;
	
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getJobKey() {
		return jobKey;
	}
	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getExeJobQueueName() {
		return exeJobQueueName;
	}
	public void setExeJobQueueName(String exeJobQueueName) {
		this.exeJobQueueName = exeJobQueueName;
	}
	public String getOwnerMobile() {
		return ownerMobile;
	}
	public void setOwnerMobile(String ownerMobile) {
		this.ownerMobile = ownerMobile;
	}
	public String getJobDataJson() {
		return jobDataJson;
	}
	public void setJobDataJson(String jobDataJson) {
		this.jobDataJson = jobDataJson;
	}
	public String getCronExpress() {
		return cronExpress;
	}
	public void setCronExpress(String cronExpress) {
		this.cronExpress = cronExpress;
	}
	public Integer getMaxDelayTime() {
		return maxDelayTime;
	}
	public void setMaxDelayTime(Integer maxDelayTime) {
		this.maxDelayTime = maxDelayTime;
	}
	public Boolean getIsAlarm() {
		return isAlarm;
	}
	public void setIsAlarm(Boolean isAlarm) {
		this.isAlarm = isAlarm;
	}
	public String getAlarmEmails() {
		return alarmEmails;
	}
	public void setAlarmEmails(String alarmEmails) {
		this.alarmEmails = alarmEmails;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getLastExeTime() {
		return lastExeTime;
	}
	public void setLastExeTime(Date lastExeTime) {
		this.lastExeTime = lastExeTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
