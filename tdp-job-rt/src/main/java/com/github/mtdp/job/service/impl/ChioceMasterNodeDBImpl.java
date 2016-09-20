package com.github.mtdp.job.service.impl;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.InetAddress;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.mtdp.job.dao.IHearbeatMapper;
import com.github.mtdp.job.dao.domain.Hearbeat;
import com.github.mtdp.job.service.IChioceMasterNode;
import com.github.mtdp.job.service.IJobContainer;
import com.github.mtdp.util.DateUtil;
import com.github.mtdp.util.SystemUtil;
/**
 * 
 *
 * @Description 选举一个master利用zk的临时节点实现
 * @author wangguoqing
 * @date 2016年8月1日下午2:54:45
 *
 */
public class ChioceMasterNodeDBImpl implements IChioceMasterNode {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("job.service.impl.JobContainerCacheImpl")
	private IJobContainer jobContainer;
	
	@Autowired
	private IHearbeatMapper hearbeatMapper;
	
	/**本机ip**/
	private String localIp;
	/**本机名称**/
	private String localName;
	/**数据库唯一约束,防止多个节点同时向数据库新增记录**/
	private String lockId = "job_lock_id";
	/**心跳最大的间隔时间 20*1000ms**/
	private long maxHearbeatTime = 20 * 1000;
	/**循环执行心跳的间隔时间 5*1000ms**/
	private long cycleIntervalTime = 5 * 1000; 
	
	/**心跳丢失master次数,连续累计3次执行更新master sql**/
	private int lostMasterCnt = 0;
	
	public ChioceMasterNodeDBImpl(){
		InetAddress inet = SystemUtil.getSystemLocalIp();
		this.localIp = inet.getHostAddress();
		this.localName = inet.getHostName();
	}
	
	@Override
	public void process() {
		new Thread(){
			@Override
			public void run() {
				logger.info("开始执行心跳服务");
				exeProcess();
			}
		}.start();
	}
	
	/**
	 * 执行处理器
	 */
	private void exeProcess(){
		//是否开启循环执行任务线程
		boolean isExe = false;
		while(true){
			Hearbeat h = this.hearbeatMapper.getHearbeatByLockId(this.lockId);
			if(h != null){
				//1.查询数据库是否有记录,如果有,查看ip及node名称与本地一样,如果一样,更新updateTime值
				if(this.localIp.equals(h.getIp()) && this.localName.equalsIgnoreCase(h.getNodeName())){
					logger.info("当前心跳服务master是本机");
					Hearbeat t = new Hearbeat();
					t.setId(h.getId());
					t.setUpdateTime(new Date());
					int c = this.hearbeatMapper.update(t);
					if(c == 1){
						isExe = true;
						//心跳丢失master次数清零
						lostMasterCnt = 0;
					}else{
						 logger.error("更新master心跳数据失败ip={},nodeName={}",this.localIp,this.localName);
					}
				}else{
					//2.如果不一样,并且更新时间超过最大心跳时间,更新数据的记录,ip,nodeName,lockId,createTime,updateTime
					if(DateUtil.calculateTwoTimeLag(new Date(), h.getUpdateTime()) > this.maxHearbeatTime){
						logger.info("当前心跳服务master不是本机,且最后更新时({})大于maxHearbeatTime={}ms,",DateUtil.formatDefault2(h.getUpdateTime()),this.maxHearbeatTime);
						++ this.lostMasterCnt;
						//丢失次数小于3次不更新master机器
						if(this.lostMasterCnt < 3){
							logger.info("心跳丢失master次数lostMasterCnt={}",this.lostMasterCnt);
						}else{
							Hearbeat t = new Hearbeat();
							t.setId(h.getId());
							t.setIp(this.localIp);
							t.setNodeName(this.localName);
							t.setLockId(this.lockId);
							t.setCreateTime(new Date());
							t.setUpdateTime(new Date());
							int c = this.hearbeatMapper.update(t);
							if(c == 1){
								isExe = true;
							}
							//TODO 2016-08-26 13:12:24 如果master机器由于线程卡死变成salve机器,并且它的查询数据库需要执行的任务线程没有停止,怎么让他停止.
							//此问题在2016-08-28部署2个节点出现
							//idea 2016-08-26 23:33:26
							//通过修改心跳前的hearbeat表ip+port通过dubbo服务通知其不再扫描数据库及修改isRun=false
							//jobContainer包装一个wapper提供dubbo服务修改jobContainer isRun 属性的方法
						}
					}else{
						logger.info("数据库的心跳主机不是本机且最后更新时间({})小于最大心跳({}ms)",DateUtil.formatDefault2(h.getUpdateTime()),this.maxHearbeatTime);
						//检查循环查询数据库需要执行的任务线程是否停止
						if(this.jobContainer.getRun()){
							this.jobContainer.setRun(false);
						}
					}
				}
			}else{
				//3.数据库没有记录,新增记录
				logger.info("数据库心跳没有数据,新增本机数据");
				Hearbeat t = new Hearbeat();
				t.setIp(this.localIp);
				t.setNodeName(this.localName);
				t.setLockId(this.lockId);
				t.setCreateTime(new Date());
				t.setUpdateTime(new Date());
				int c = this.hearbeatMapper.add(t);
				if(c == 1){
					isExe = true;
				}
			}
			
			this.exeJobContainer(isExe);
		}
	}
	
	/**
	 * 调用循环查询数据库需要执行的任务线程
	 * @param isExe
	 */
	private void exeJobContainer(boolean isExe){
		//判断是否需要调用任务容器 循环查询数据库需要执行的任务线程
		if(isExe){
			if(!this.jobContainer.getRun()){
				jobContainer.setRun(true);
				Thread t = new Thread(){
					public void run() {
						jobContainer.process();
					};
				};
				//处理t线程出现异常情况
				t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						logger.info("任务执行容器初始化异常t={},e={}",t,e);
						//设置jobContanier的isRun=false,下次心跳执行时继续执行
						jobContainer.setRun(false);
					}
				});
				t.start();
			}else{
				logger.info("不需要修改isRun的值,isRun={}",this.jobContainer.getRun());
			}
		}else{
			jobContainer.setRun(false);
		}
		try {
			logger.info("循环执行心跳线程开始休息sleepTime={}ms",this.cycleIntervalTime);
			Thread.sleep(this.cycleIntervalTime);
			logger.info("循环执行心跳线程休眠结束sleepTime={}ms",this.cycleIntervalTime);
		} catch (InterruptedException e) {
			logger.error("循环执行心跳线程被打断sleepTime={}ms",this.cycleIntervalTime);
		}
	}
	
	public long getMaxHearbeatTime() {
		return maxHearbeatTime;
	}
	public void setMaxHearbeatTime(long maxHearbeatTime) {
		this.maxHearbeatTime = maxHearbeatTime;
	}
	public long getCycleIntervalTime() {
		return cycleIntervalTime;
	}
	public void setCycleIntervalTime(long cycleIntervalTime) {
		this.cycleIntervalTime = cycleIntervalTime;
	}
	
	
	public static void main(String[] args) throws Exception {
		ChioceMasterNodeDBImpl c = new ChioceMasterNodeDBImpl();
		c.process();
	}

}
