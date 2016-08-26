package com.github.mtdp.job.demo;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mtdp.job.api.TdpJobAbstractClient;
import com.github.mtdp.job.api.bean.JobDetailBean;
/**
 * 
 *
 * @Description tdp job 实现demo
 * @author wangguoqing
 * @date 2016年8月23日上午10:21:22
 *
 */
public class TdpJobDemoClient extends TdpJobAbstractClient{
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean execute(JobDetailBean message) {
		logger.info("执行demo job");
		try {
			Thread.sleep(1000L);
			Random r = new Random();
			if(r.nextBoolean()){
				//随机任务处理出错
				throw new RuntimeException("随机处理出错");
			}
		} catch (InterruptedException e) {
			logger.error("休息被打断",e);
		}
		return true;
	}
	
	public static void main(String[] args) {
		Random r = new Random();
		System.out.println(r.nextBoolean());
		System.out.println(r.nextBoolean());
		System.out.println(r.nextBoolean());
		System.out.println(r.nextBoolean());
	}
}
