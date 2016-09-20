package com.github.mtdp.job.test;

import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.mtdp.job.dao.IJobDetailMapper;
import com.github.mtdp.job.dao.domain.JobDetail;


public class MapperTest  extends BaseTest{
	
	private static final Logger logger = LoggerFactory.getLogger(MapperTest.class);
	
	@Autowired
	private IJobDetailMapper detailMapper;

	@Test
	public void get(){
		JobDetail d = this.detailMapper.get(1L);
		logger.info("*********d={}",d);
		logger.info("*********d.isAlarm()={}",d.getIsAlarm());
		
		JobDetail dd = new JobDetail();
		dd.setJobId(d.getJobId());
		dd.setIsAlarm(false);
		dd.setUpdateTime(new Date());
		int cnt = this.detailMapper.update(dd);
		logger.info("*********cnt={}",cnt);
	}
	
}
