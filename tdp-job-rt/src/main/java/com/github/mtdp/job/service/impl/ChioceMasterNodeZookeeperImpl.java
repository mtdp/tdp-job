package com.github.mtdp.job.service.impl;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.mtdp.job.service.IChioceMasterNode;
import com.github.mtdp.job.service.IJobContainer;
/**
 * 
 *
 * @Description 选举一个master利用zk的临时节点实现
 * @author wangguoqing
 * @date 2016年8月1日下午2:54:45
 *
 */
public class ChioceMasterNodeZookeeperImpl implements IChioceMasterNode {
	
	@Autowired
	private IJobContainer jobContainer;
	
	private String path = "/tdp-job";
	
	private String connectZookeeperString = "192.168.10.151:2181"; 

	@Override
	public void process() {
		boolean isExe = false;
		//TODO 1.连接zk
		
		if(isExe){
			jobContainer.setRun(true);
			jobContainer.process();
		}else{
			jobContainer.setRun(false);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ChioceMasterNodeZookeeperImpl c = new ChioceMasterNodeZookeeperImpl();
		
		CuratorFramework client = CuratorFrameworkFactory.newClient(c.connectZookeeperString, new RetryNTimes(2,1000));
		client.start();
		
		client.create().forPath(c.path);
		
		
		System.out.println("111");
	}

}
