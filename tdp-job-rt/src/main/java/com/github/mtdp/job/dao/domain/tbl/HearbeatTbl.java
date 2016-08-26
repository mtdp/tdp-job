package com.github.mtdp.job.dao.domain.tbl;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @Description 心跳检测实体
 * @author wangguoqing
 * @date 2016年7月30日下午1:33:21
 *
 */
public class HearbeatTbl implements Serializable {

	private static final long serialVersionUID = 5235886797696158489L;
	
	private Long id;
	/**节点名称**/
	private String nodeName;
	/**节点ip**/
	private String ip;
	/**心跳端口**/
	private Integer port;
	/**锁id,防止并发,数据库设置unique约束**/
	private String lockId;
	private Date createTime;
	private Date updateTime;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
