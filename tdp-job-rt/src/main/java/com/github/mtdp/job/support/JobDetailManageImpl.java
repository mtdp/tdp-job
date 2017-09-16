package com.github.mtdp.job.support;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.github.mtdp.job.api.JobConstantsCode;
import com.github.mtdp.job.dao.IJobDetailMapper;
import com.github.mtdp.job.dao.domain.JobDetail;
import com.github.mtdp.job.service.ICacheService;
import com.github.mtdp.job.service.IJobContainer;
import com.github.mtdp.job.util.CronExpressUtil;
import com.github.mtdp.persistence.BasePagination;


@Service("job.support.JobDetailManageImpl")
public class JobDetailManageImpl implements IJobDetailManage {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private IJobDetailMapper jobDetailMapper;
	
	@Autowired
	private ICacheService cacheService;
	
	
	@Autowired
	@Qualifier("job.service.impl.JobContainerCacheImpl")
	private IJobContainer jobContainer;

	@Override
	public BasePagination<SupportJobDetailBean> searchJobPagination(BasePagination<SupportJobDetailBean> jobPage) {
		logger.info("job分页开始");
		Map<String, Object> param = jobPage.getQueryParams();
		int totalRecord = this.jobDetailMapper.searchJobCount(param);
		jobPage.setTotalRecord(totalRecord);
		//查询条件
		param.put("sort", "updateTime");
		List<JobDetail> jobs = this.jobDetailMapper.searchJobPagination(param);
		List<SupportJobDetailBean> mJob = new ArrayList<SupportJobDetailBean>();
		//bean 转换 
		for(JobDetail t : jobs){
			SupportJobDetailBean bean = new SupportJobDetailBean();
			BeanUtils.copyProperties(t, bean);
			bean.setAlarm(t.getIsAlarm());
			Date nextExeTime = CronExpressUtil.getLastTime(t.getCronExpress(), new Date());
			bean.setNextExeTime(nextExeTime);
			mJob.add(bean);
		}
		jobPage.setResults(mJob);
		//rpc 返回结果序列化返回值
		return jobPage;
	}

	@Override
	public SupportJobDetailBean getJob(Long id) {
		JobDetail job = this.jobDetailMapper.get(id);
		SupportJobDetailBean bean = new SupportJobDetailBean();
		BeanUtils.copyProperties(job, bean);
		bean.setAlarm(job.getIsAlarm());
		Date d = new Date();
		Date nextExeTime = CronExpressUtil.getLastTime(job.getCronExpress(),d);
		bean.setNextExeTime(nextExeTime);
		bean.setLastExeTimes(CronExpressUtil.getLastFiveTime(job.getCronExpress(), d));
		return bean;
	}

	@Override
	public boolean updateJob(SupportJobDetailBean mBean) {
		JobDetail job = new JobDetail();
		BeanUtils.copyProperties(mBean, job);
		job.setIsAlarm(mBean.isAlarm());
		job.setUpdateTime(new Date());
		int cnt = this.jobDetailMapper.update(job);
		if(cnt == 1){
			//清除缓存,让缓存更新
			this.cacheService.clearCache(JobConstantsCode.JOB_DETAIL_CACHEKEY);
			return true;
		}
		return false;
	}

	@Override
	public boolean saveJob(SupportJobDetailBean mBean) {
		JobDetail job = new JobDetail();
		BeanUtils.copyProperties(mBean, job);
		job.setIsAlarm(mBean.isAlarm());
		job.setCreateTime(new Date());
		job.setUpdateTime(new Date());
		int cnt = this.jobDetailMapper.add(job);
		if(cnt == 1){
			//清除缓存,让缓存更新
			this.cacheService.clearCache(JobConstantsCode.JOB_DETAIL_CACHEKEY);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean manualExeJob(SupportJobDetailBean mBean) {
		logger.info("手动执行任务");
		JobDetail job = new JobDetail();
		BeanUtils.copyProperties(mBean, job);
		job.setIsAlarm(mBean.isAlarm());
		this.jobContainer.exeJob(job);
		return true;
	}

}
