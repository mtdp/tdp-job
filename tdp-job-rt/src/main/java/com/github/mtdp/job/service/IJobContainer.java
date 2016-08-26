package com.github.mtdp.job.service;

import com.github.mtdp.job.dao.domain.JobDetail;

/**
 * 
 *
 * @Description 任务执行的容器
 * @author wangguoqing
 * @date 2016年7月30日下午4:06:04
 *
 */
public interface IJobContainer {
	
	/**
	 * 循环获取所有需要执行的任务,处理并调用执行符合条件的任务
	 */
	public void process();
	
	/**
	 * 执行具体某个任务
	 * @param job
	 */
	public void exeJob(JobDetail job);
	
	/**
	 * 设置处理器是否循环执行 true=是
	 * @param isRun
	 */
	public void setRun(boolean isRun);
	
	/**
	 * 获取是否循环执行 true=是
	 * @return
	 */
	public boolean getRun();

}
