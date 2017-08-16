package com.junranhuigu.simpleNote.vo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.junranhuigu.simpleNote.util.StringUtil;

public class SimpleNote {
	public static String LINE_CHAR = "\n";//换行标识符
	
	/**
	 * 简易信息
	 * */
	public static String simpleNote(PhotoInfo info) throws Exception{
		StringBuilder builder = new StringBuilder();
		if(info.getTime() != null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			builder.append(sdf.format(info.getTime())).append(LINE_CHAR);
		}
		if(!StringUtil.isEmpty(info.getWeather())){
			Pattern pattern = Pattern.compile("[0-9]+年[0-9]+月[0-9]+日");
			String weather = pattern.matcher(info.getWeather()).replaceAll("");
			builder.append(weather).append(LINE_CHAR);
		}
		if(info.getAddress() != null){
			builder.append(info.getAddress().simpleDetail()).append(LINE_CHAR);
		}
		builder.delete(builder.lastIndexOf(LINE_CHAR), builder.length());
		return builder.toString();
	}
	
	/**
	 * 地图显示信息
	 * */
	public static String mapNote(PhotoInfo info) throws Exception{
		Map<String, String> params = new HashMap<>();
		params.put("info", simpleNote(info));
		params.put("path", info.showPath().replace(File.separator, "/"));
		params.put("position", info.getAddress().getMapLocations());
		return JSON.toJSONString(params);
	}
}
