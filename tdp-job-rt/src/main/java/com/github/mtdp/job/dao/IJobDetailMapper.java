package com.github.mtdp.job.dao;

import java.util.List;

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

}
