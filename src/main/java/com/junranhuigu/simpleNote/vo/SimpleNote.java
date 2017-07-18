package com.junranhuigu.simpleNote.vo;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import com.junranhuigu.simpleNote.util.StringUtil;

public class SimpleNote {
	/**
	 * 简易信息
	 * */
	public static String simpleNote(PhotoInfo info) throws Exception{
		StringBuilder builder = new StringBuilder();
		if(info.getTime() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			builder.append(sdf.format(info.getTime())).append("\t");
		}
		if(info.getAddress() != null){
			builder.append(info.getAddress().getDetail()).append("\t");
		}
		if(!StringUtil.isEmpty(info.getWeather())){
			Pattern pattern = Pattern.compile("[0-9]+年[0-9]+月[0-9]+日");
			String weather = pattern.matcher(info.getWeather()).replaceAll("");
			builder.append("天气：").append(weather);
		}
		return builder.toString();
	}
}
