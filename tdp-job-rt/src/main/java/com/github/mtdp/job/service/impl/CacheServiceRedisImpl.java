package com.github.mtdp.job.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.mtdp.job.dao.IJobDetailMapper;
import com.github.mtdp.job.dao.domain.JobDetail;
import com.github.mtdp.job.service.ICacheService;
import com.github.mtdp.persistence.RedisManager;
/**
 * 
 *
 * @Description 缓存redis实现
 * @author wangguoqing
 * @date 2016年8月30日下午6:12:19
 *
 */

@Service("service.impl.CacheServiceRedisImpl")
public class CacheServiceRedisImpl implements ICacheService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IJobDetailMapper jobDetailMapper;
	
	@Autowired
	private RedisManager redisManager;
	/**缓存过期时间**/
	private int expire = 1800;
	
	@Override
	public List<JobDetail> getNeedExeJobDetails(String jobDetailCacheKey) {
		String jobDetailsJson = this.redisManager.get(jobDetailCacheKey);
		logger.info("缓存中的数据是jobDetailsJson={}",jobDetailsJson);
		if(jobDetailsJson == null){
			logger.info("缓存key={}没有数据,查询数据库",jobDetailCacheKey);
			List<JobDetail> jobDetails = this.jobDetailMapper.gets();
			if(jobDetails != null && !jobDetails.isEmpty()){
				jobDetailsJson = JSON.toJSONString(jobDetails);
				this.redisManager.set(jobDetailCacheKey, jobDetailsJson,this.expire);
				return jobDetails;
			}
		}
		return JSON.parseArray(jobDetailsJson,JobDetail.class);
	}
	
	@Override
	public void updateValue4Key(String key, Object obj) {
		logger.info("更新key={}的缓存数据",key);
		String value = JSON.toJSONString(obj);
		this.redisManager.set(key, value);
	}

}
