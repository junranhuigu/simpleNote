package com.junranhuigu.simpleNote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WeatherUtil {
	
	/**
	 * 获取历史天气
	 * @param date 格式:yyyyMMdd
	 * @return 天气信息
	 * */
	public static String historyWeather(String city, String date) throws Exception{
		if(StringUtil.isEmpty(city) || StringUtil.isEmpty(date)){
			return null;
		}
		String cityName = cityCode(city);//城市代码
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date time = sdf.parse(date);//日期
		
		//获取历史天气网址
		String path = "http://www.tianqihoubao.com/lishi/%s/month/%s.html";
		sdf = new SimpleDateFormat("yyyyMM");
		path = String.format(path, cityName, sdf.format(time));
		//获取天气数据
		String resp = ChannelUtil.httpGet(path, "GBK");
		Document doc = Jsoup.parse(resp);
		sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String dateCode = sdf.format(time);
		for(Element e : doc.getElementById("content").select("tr")){
			if(e.text().contains(dateCode)){
				return e.text();
			}
		}
		return null;
	}
	
	/**
	 * 获取历史天气
	 * @param date 格式:yyyyMMdd
	 * @return 天气信息 key：日期 value：天气信息
	 * */
	public static Map<String, String> historyWeather(String cityCode, Set<String> dates) throws Exception{
		if(StringUtil.isEmpty(cityCode) || dates == null || dates.isEmpty()){
			return null;
		}
//		String cityName = cityCode(city);//城市代码
		String cityName = cityCode;
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
		//按月分组日期
		Separator<String, String> separator = new Separator<String, String>() {
			@Override
			public String separate(String v) {
				try {
					Date date = sdf.parse(v);
					return sdf2.format(date);
				} catch (Exception e) {}
				return "";
			}
		};
		separator.addAll(dates);
		HashMap<String, List<String>> months = separator.getResult();
		Map<String, String> result = new HashMap<>();
		for(String month : months.keySet()){
			//获取历史天气网址
			String path = "http://www.tianqihoubao.com/lishi/%s/month/%s.html";
			path = String.format(path, cityName, month);
			//获取天气数据
			String resp = ChannelUtil.httpGet(path, "GBK");
			Document doc = Jsoup.parse(resp);
			for(String time : months.get(month)){
				Date _d = sdf.parse(time);
				SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy年MM月dd日");
				String dateCode = sdf3.format(_d);
				for(Element e : doc.getElementById("content").select("tr")){
					if(e.text().contains(dateCode)){
						String weather = e.text();
						result.put(time, weather);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取城市代号
	 * */
	private static String cityCode(String city) throws Exception{
		String path = "http://www.tianqihoubao.com/lishi/";
		String resp = ChannelUtil.httpGet(path, "GBK");
		Document doc = Jsoup.parse(resp);
		return cityCode(doc, city);
	} 
	
	/**
	 * 获取城市代号
	 * @return key城市名 value城市代号
	 * */
	public static Map<String, String> cityCode(Set<String> cities) throws Exception{
		if(cities == null || cities.isEmpty()){
			return new HashMap<>(1);
		}
		String path = "http://www.tianqihoubao.com/lishi/";
		String resp = ChannelUtil.httpGet(path, "GBK");
		Document doc = Jsoup.parse(resp);
		Map<String, String> result = new HashMap<>();
		for(String city : cities){
			String code = cityCode(doc, city);
			if(!StringUtil.isEmpty(code)){
				result.put(city, code);
			}
		}
		return result;
	}
	
	private static String cityCode(Document doc, String city){
		ArrayList<String> codes = new ArrayList<>();
		for(Element e : doc.select("a")){
			if(city.contains(e.text()) && e.attr("href").contains("lishi")){
				String content = e.attr("href");
				String code = content.substring(content.lastIndexOf("/") + 1, content.lastIndexOf("."));
				if(!StringUtil.isEmpty(code)){
					codes.add(code);
				}
			}
		}
		if(codes.isEmpty()){
			return null;
		} else {
			Collections.sort(codes, new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					if(o1.length() > o2.length()){
						return -1;
					} else if(o1.length() < o2.length()){
						return 1;
					} else {
						return 0;
					}
				}
			});
			return codes.get(0);
		}
	}
}
