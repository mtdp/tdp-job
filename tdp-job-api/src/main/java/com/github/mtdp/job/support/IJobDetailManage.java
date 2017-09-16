package com.github.mtdp.job.support;

import com.github.mtdp.persistence.BasePagination;

/**
 * 
 *
 * @Description 任务管理
 * @author wangguoqing
 * @date 2016年8月23日下午2:47:33
 *
 */
public interface IJobDetailManage {
	
	/**
	 * 任务分页
	 * @param channelPage
	 * @return 查询处理的结果
	 */
    public BasePagination<SupportJobDetailBean> searchJobPagination(BasePagination<SupportJobDetailBean> jobPage);
    
    /**
     * 获取任务
     * @param id
     * @return
     */
    public SupportJobDetailBean getJob(Long id);
    
    /**
     * 更新任务
     * @param mBean
     */
    public boolean updateJob(SupportJobDetailBean mBean);
    
    /**
     * 新增任务
     * @param mBean
     * @return
     */
    public boolean saveJob(SupportJobDetailBean mBean);
    
    /**
     * 手动执行任务
     * @param mBean
     * @return
     */
    public boolean manualExeJob(SupportJobDetailBean mBean);
}
