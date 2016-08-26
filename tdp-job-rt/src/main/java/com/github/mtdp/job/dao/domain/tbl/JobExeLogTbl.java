package com.github.mtdp.job.dao.domain.tbl;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @Description 任务执行的日志
 * @author wangguoqing
 * @date 2016年7月30日下午1:21:35
 *
 */
public class JobExeLogTbl implements Serializable{

	private static final long serialVersionUID = 2080845864441192420L;
	
	private Long logId;
	/**任务id**/
	private Long jobId;
	/**任务key**/
	private String jobKey;
	/**任务名称**/
	private String jobName;
	/**执行任务的节点名称**/
	private String nodeName;
	/**根据cron表达式,计划执行时间**/
	private Date planExeTime;
	/**执行任务开始时间**/
	private Date startExeTime;
	/**执行任务结束时间**/
	private Date endExeTime;
	/**执行任务的状态:5=初始(执行中),10=执行成功,15=执行失败**/
	private Integer status;
	/**是否发送过报警消息:邮件或者短信**/
	private Boolean isSendAlarmMsg;
	/**备注字段**/
	private String memo;
	/**更新任务结束时使用**/
	private String logUUID;
	private Date createTime;
	private Date updateTime;
	
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
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
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public Date getPlanExeTime() {
		return planExeTime;
	}
	public void setPlanExeTime(Date planExeTime) {
		this.planExeTime = planExeTime;
	}
	public Date getStartExeTime() {
		return startExeTime;
	}
	public void setStartExeTime(Date startExeTime) {
		this.startExeTime = startExeTime;
	}
	public Date getEndExeTime() {
		return endExeTime;
	}
	public void setEndExeTime(Date endExeTime) {
		this.endExeTime = endExeTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Boolean isSendAlarmMsg() {
		return isSendAlarmMsg;
	}
	public void setSendAlarmMsg(Boolean isSendAlarmMsg) {
		this.isSendAlarmMsg = isSendAlarmMsg;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLogUUID() {
		return logUUID;
	}
	public void setLogUUID(String logUUID) {
		this.logUUID = logUUID;
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
