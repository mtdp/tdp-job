package com.github.mtdp.job.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.CronExpression;

/**
 * 
 *
 * @Description cron表达式解析工具
 * @author wangguoqing
 * @date 2016年7月30日下午2:59:51
 *
 */
public class CronExpressUtil {
	
	private static final String CRON_EXPRESS = "ss mm HH dd MM ? yyyy";

	/**
	 * 获取cron表达式最近一次的执行时间
	 * @param cronExpress
	 * @return
	 */
	public static Date getLastTime(String cronExpress,Date date){
		Date d = null;
		try{
			CronExpression cp = new CronExpression(cronExpress);
			//getNextValidTimeAfter getNextInvalidTimeAfter 2个方法的区别 是否有使用默认时区？
			d = cp.getNextValidTimeAfter(date);
		}catch(ParseException e){
			throw new RuntimeException("quartz解析cron表达式出错cronExpress="+cronExpress);
		}
		return d;
	}
	
	/**
	 * 计算并返回给的cron表达式最近5个执行的时间点
	 * @param cronExpress
	 * @param date
	 * @return
	 */
	public static List<Date> getLastFiveTime(String cronExpress,Date date){
		List<Date> dates = new ArrayList<Date>();
		try{
			int cnt = 5;
			CronExpression cp = new CronExpression(cronExpress);
			//模拟时间走5秒
			for(int i = 0; i < cnt; i++){
				Date d = cp.getNextValidTimeAfter(date);
				if(d == null){
					continue;
				}
				dates.add(d);
				//设置时间为
				date.setTime(d.getTime());
			}
			
		}catch(ParseException e){
			throw new RuntimeException("quartz解析cron表达式出错cronExpress="+cronExpress);
		}
		return dates;
	}
	
	/**
	 * 星期没有处理
	 * @param d
	 * @return
	 */
	public static String getCron(Date d){
		String cron = null;
		SimpleDateFormat sdf = new SimpleDateFormat(CRON_EXPRESS);
		cron = sdf.format(d);
		return cron;
	}
	
	public static void main(String[] args) throws ParseException {
		String cronExp = "* * * * * ? *";
		cronExp = "59 15 10 18 * ?";
		cronExp = "0 14 15 ? * *";
		cronExp = "0 30 10 22 * ? 2017";
		cronExp = "0 30 10 22 12,1 ? 2017,2018";
		cronExp = "13 25 16 21 * ? 2017 ";
		Date d = new Date();
		System.out.println(getLastTime(cronExp, d));
		System.out.println(getLastFiveTime(cronExp, d));
		System.out.println(getCron(d));
	}
	
}
