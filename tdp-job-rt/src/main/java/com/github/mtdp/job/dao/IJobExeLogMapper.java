package com.github.mtdp.job.dao;

import com.github.mtdp.job.dao.domain.JobExeLog;
import com.github.mtdp.persistence.BaseMapper;
/**
 * 
 *
 * @Description 任务执行日志dao接口
 * @author wangguoqing
 * @date 2016年7月31日上午8:53:56
 *
 */
public interface IJobExeLogMapper extends BaseMapper<JobExeLog,Long> {
	
	/**
	 * 根据日志的uuid更新任务日志
	 * @param log
	 * @return
	 */
	public int updateJobLogByUUID(JobExeLog log);
	

}
