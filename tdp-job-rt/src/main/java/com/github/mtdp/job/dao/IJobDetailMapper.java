package com.github.mtdp.job.dao;

import java.util.List;
import java.util.Map;

import com.github.mtdp.job.dao.domain.JobDetail;
import com.github.mtdp.persistence.BaseMapper;
/**
 * 
 *
 * @Description 任务实体dao接口
 * @author wangguoqing
 * @date 2016年7月30日下午9:29:50
 *
 */
public interface IJobDetailMapper extends BaseMapper<JobDetail,Long> {
	/**
	 * 获取需要执行任务列表,启用,最近一次执行时间小于当前时间
	 * @returnI
	 */
	public List<JobDetail> getNeedExeJobDetails();
	
	/**
	 * 获取状态为启用的任务
	 * @return
	 */
	public List<JobDetail> getEnableJobDetails();
	
	
	/**
	 * 任务分页
	 * @param map
	 * @return
	 */
	public List<JobDetail> searchJobPagination(Map<String, Object> map);
	public Integer searchJobCount(Map<String, Object> map);

}
