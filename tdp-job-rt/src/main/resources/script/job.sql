/*
Navicat MySQL Data Transfer

Source Server         : 127.0.0.1
Source Server Version : 50610
Source Host           : localhost:3306
Source Database       : tdp

Target Server Type    : MYSQL
Target Server Version : 50610
File Encoding         : 65001

Date: 2016-09-02 14:43:56
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
  `maxDelayTime` int(11) DEFAULT '0' COMMENT '任务接受的最大延迟执行时间,单位秒;默认值=0,不延迟',
  `isAlarm` tinyint(4) DEFAULT '1' COMMENT '是否需要报警1=true,0=false',
  `alarmEmails` varchar(256) DEFAULT NULL COMMENT '报警接受的邮件列表,多个用;隔开',
  `status` tinyint(4) DEFAULT NULL COMMENT '5=禁用,10=启用',
  `lastExeTime` datetime DEFAULT NULL COMMENT '最后执行时间',
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  PRIMARY KEY (`jobId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
