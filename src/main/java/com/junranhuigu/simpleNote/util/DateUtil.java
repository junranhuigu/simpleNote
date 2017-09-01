package com.junranhuigu.simpleNote.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	/**
	 * 验证日期是否是在今天
	 * */
	public static boolean isInToday(Date time){
		String timeStr = parseDate(time);
		String nowStr = parseDate(new Date());
		return timeStr.equals(nowStr);
	}
	
	/**
	 * 转换字符串为日期格式
	 * @param time 格式yyyy-MM-dd HH:mm:ss
	 * */
	public static Date parseTime(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = sdf.parse(time);
			return date;
		} catch (Exception e) {
			System.err.println("转换时间失败");
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 转换日期为字符串格式
	 * @return 格式yyyy-MM-dd HH:mm:ss
	 * */
	public static String parseTime(Date time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.format(time);
		} catch (Exception e) {
			System.err.println("转换时间失败");
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 转换字符串为日期格式
	 * @param time 格式yyyy-MM-dd
	 * */
	public static Date parseDate(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = sdf.parse(time);
			return date;
		} catch (Exception e) {
			System.err.println("转换时间失败");
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 转换日期为字符串格式
	 * @return 格式yyyy-MM-dd
	 * */
	public static String parseDate(Date time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.format(time);
		} catch (Exception e) {
			System.err.println("转换时间失败");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查看相差的天数 
	 * */
	public static int betweenDate(long currTime, long oldTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String begin = sdf.format(new Date(oldTime)); 
		String end = sdf.format(new Date(currTime));
		try {
			long day = (sdf.parse(end).getTime() - sdf.parse(begin).getTime()) / (24 * 3600 * 1000L);
			return (int) (day < 0 ? 0 : day);
		} catch (Exception e) {}
		return 0;
	}
	
	/**
	 * 获取本周X的日期
	 * @param time 时间锚
	 * @param weekDay 周X 推荐使用Calendar的星期常量表示 例：周一即Calendar.MONDAY
	 * */
	public static Date weekday(long time, int weekDay){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.set(Calendar.DAY_OF_WEEK, weekDay);
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期的星期数
	 * @return 1-6表示周一-周六 0表示周日
	 * */
	public static int weekday(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}
}
