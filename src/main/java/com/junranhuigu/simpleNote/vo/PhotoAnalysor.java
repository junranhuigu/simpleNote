package com.junranhuigu.simpleNote.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.junranhuigu.simpleNote.structure.Separator;
import com.junranhuigu.simpleNote.util.WeatherUtil;

public class PhotoAnalysor {
	
	public static void main(String[] args) throws Exception{
		List<String> paths = new ArrayList<>();
	    paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170423_175914.jpg");
	    paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170205_110835.jpg");
	    paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170625_150057_HDR.jpg");
	    paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170625_145122_HDR.jpg");
	    paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170718_141845.jpg");
	    paths.add("C:\\Users\\jiawei\\Desktop\\IMG_20170718_141909.jpg");
	    paths.add("C:\\Users\\jiawei\\Desktop\\问题导向的知识管理.png");
	    paths.add("C:\\Users\\jiawei\\Desktop\\微信图片_20170705112327.jpg");
	    for(PhotoInfo info : analysis(paths)){
	    	System.out.println(JSON.toJSONString(info));
		    System.out.println(SimpleNote.simpleNote(info));
	    }
	}
	
	public static List<PhotoInfo> analysis(List<String> paths) throws Exception{
		if(paths == null || paths.isEmpty()){
			return Collections.emptyList();
		}
		List<PhotoInfo> infos = new ArrayList<>();
		Separator<String, PhotoInfo> separatorByAddress = new Separator<String, PhotoInfo>() {
			@Override
			public String separate(PhotoInfo info) {
				return info.positionName();
			}
		};
		for(String path : paths){
			PhotoInfo info = new PhotoInfo(path);
			separatorByAddress.add(info);
			infos.add(info);
		}
		//准备地址与日期信息
		Iterator<Entry<String, List<PhotoInfo>>> ite = separatorByAddress.getResult().entrySet().iterator();
		Map<String, Set<String>> query = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		while(ite.hasNext()){
			Entry<String, List<PhotoInfo>> entry = ite.next();
			Set<String> times = new HashSet<>();
			for(PhotoInfo info : entry.getValue()){
				if(info.getTime() != null){
					times.add(sdf.format(info.getTime()));
				}
			}
			query.put(entry.getKey(), times);
		}
		//准备天气数据
		Map<String, String> cityCodes = WeatherUtil.cityCode(query.keySet());
		Iterator<Entry<String, Set<String>>> ite2 = query.entrySet().iterator();
		while(ite2.hasNext()){
			Entry<String, Set<String>> entry = ite2.next();
			Map<String, String> weathers = WeatherUtil.historyWeather(cityCodes.get(entry.getKey()), entry.getValue());
			for(PhotoInfo info : infos){
				if(entry.getKey().equals(info.positionName())){//对应地区
					String date = sdf.format(info.getTime());
					info.setWeather(weathers.get(date));
				}
			}
		}
		return infos;
	}
}
