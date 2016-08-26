package com.github.mtdp.job.api;

import com.github.mtdp.job.api.bean.JobDetailBean;

/**
 * 
 *
 * @Description tdp任务客户端接口
 * @author wangguoqing
 * @date 2016年6月3日下午12:44:37
 *
 */
public interface ITdpJobClient {
	
	/**
	 * 执行任务的方法
	 * @param message
	 * @return
	 */
    public boolean execute(JobDetailBean message);

}
