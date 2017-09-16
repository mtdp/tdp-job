package com.github.mtdp.job.service;

import java.util.List;

import com.github.mtdp.job.dao.domain.JobDetail;

/**
 * 
 *
 * @Description 缓存接口
 * @author wangguoqing
 * @date 2016年8月30日下午6:10:17
 *
 */
public interface ICacheService {
	
	/**
	 * 获取需要执行的任务列表,缓存没有数据,从数据库获取
	 * @param jobDetailCacheKey
	 * @return
	 */
	public List<JobDetail> getNeedExeJobDetails(String jobDetailCacheKey);
	
	/**
	 * 更新key的值
	 * @param key
	 * @param obj
	 */
	public void updateValue4Key(String key,Object obj);
	
	/**
	 * 清除缓存数据
	 * @param key
	 */
	public void clearCache(String key);

}
