package com.github.mtdp.job.dao;

import org.apache.ibatis.annotations.Param;

import com.github.mtdp.job.dao.domain.Hearbeat;
import com.github.mtdp.persistence.BaseMapper;
/**
 * 
 *
 * @Description 心跳接口dao接口
 * @author wangguoqing
 * @date 2016年7月31日上午8:54:43
 *
 */
public interface IHearbeatMapper extends BaseMapper<Hearbeat,Long> {
	
	/**
	 * 根据锁id获取心跳数据
	 * @param lockId
	 * @return
	 */
	public Hearbeat getHearbeatByLockId(@Param("lockId")String lockId);
	
	/**
	 * 根据锁id删除数据
	 * @param lockId
	 * @return
	 */
	public int delByLockId(@Param("lockId")String lockId);

}
