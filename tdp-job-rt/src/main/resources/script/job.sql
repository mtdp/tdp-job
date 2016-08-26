/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50610
Source Host           : localhost:3306
Source Database       : tdp

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2016-08-26 12:02:33
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_job_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_job_detail`;
CREATE TABLE `t_job_detail` (
  `jobId` bigint(20) NOT NULL AUTO_INCREMENT,
  `jobKey` varchar(64) DEFAULT NULL COMMENT '任务key',
  `jobName` varchar(256) DEFAULT NULL,
  `appName` varchar(256) DEFAULT NULL,
  `exeJobQueueName` varchar(128) DEFAULT NULL COMMENT '执行任务队列名称',
  `ownerMobile` varchar(32) DEFAULT NULL COMMENT '负责人的手机号码',
  `jobDataJson` varchar(2048) DEFAULT NULL COMMENT '执行任务所需要的参数json串',
  `cronExpress` varchar(16) DEFAULT NULL COMMENT '执行任务的cron表达式',
  `isAlarm` tinyint(4) DEFAULT '1' COMMENT '是否需要报警1=true,0=false',
  `alarmEmails` varchar(256) DEFAULT NULL COMMENT '报警接受的邮件列表,多个用;隔开',
  `status` tinyint(4) DEFAULT NULL COMMENT '5=禁用,10=启用',
  `lastExeTime` datetime DEFAULT NULL COMMENT '最后执行时间',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_exe_log
-- ----------------------------
DROP TABLE IF EXISTS `t_job_exe_log`;
CREATE TABLE `t_job_exe_log` (
  `logId` bigint(20) NOT NULL AUTO_INCREMENT,
  `jobId` bigint(20) DEFAULT NULL COMMENT '任务id',
  `jobKey` varchar(256) DEFAULT NULL COMMENT '任务key',
  `jobName` varchar(256) DEFAULT NULL COMMENT '任务名称',
  `nodeName` varchar(256) DEFAULT NULL COMMENT '执行任务的节点名称',
  `planExeTime` datetime DEFAULT NULL COMMENT '根据cron表达式,理论计划执行时间',
  `startExeTime` datetime DEFAULT NULL COMMENT '执行任务开始时间',
  `endExeTime` datetime DEFAULT NULL COMMENT '执行任务结束时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '执行任务的状态:5=初始(执行中),10=执行成功,15=执行失败',
  `isSendAlarmMsg` tinyint(4) DEFAULT NULL COMMENT '是否发送过报警消息:邮件或者短信',
  `memo` varchar(4096) DEFAULT NULL COMMENT '备注',
  `logUUID` varchar(128) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`logId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_job_hearbeat
-- ----------------------------
DROP TABLE IF EXISTS `t_job_hearbeat`;
CREATE TABLE `t_job_hearbeat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nodeName` varchar(128) DEFAULT NULL,
  `ip` varchar(20) DEFAULT NULL,
  `port` varchar(8) DEFAULT NULL,
  `lockId` varchar(64) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
