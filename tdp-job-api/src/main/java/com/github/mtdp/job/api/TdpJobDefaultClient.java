package com.github.mtdp.job.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtdp.job.api.bean.JobDetailBean;
/**
 * 
 *
 * @Description 任务默认实现demo
 * @author wangguoqing
 * @date 2016年6月3日下午12:42:59
 *
 */
public class TdpJobDefaultClient extends TdpJobAbstractClient{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public boolean execute(JobDetailBean message) {
		logger.info("执行任务key={}",message.getJobKey());
		try {
			//默认休息3s
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			logger.error("模拟执行任务出错",e);
		}
		return true;
	}

}
