package com.junranhuigu.simpleNote;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class MapUtil {
	private static String mapKey = "0c7726ca1a05b5b0fc8ccff634d31eb7";
	
	public static Address mapPosition(Position position) throws Exception{
    	String path = "http://restapi.amap.com/v3/geocode/regeo?parameters";
    	Map<String, String> params = new HashMap<>();
    	params.put("key", mapKey);
    	params.put("location", convertPosition(position));
    	
    	String respStr = ChannelUtil.httpPost(path, ChannelUtil.getSortQueryString(params));
    	JSONObject resp = JSON.parseObject(respStr);
    	if(resp.getInteger("status") == 1){
    		return new Address(resp.getJSONObject("regeocode"));
    	} else {
    		System.out.println("定位" + resp.getString("info") + "，错误代码：" + resp.getInteger("status"));
    		return null;
    	}
    }
    
    /**
     * @param longtitude 经度
     * @param latitude 纬度
     * */
    private static String convertPosition(Position position) throws Exception{
    	String path = "http://restapi.amap.com/v3/assistant/coordinate/convert?parameters";
    	Map<String, String> params = new HashMap<>();
    	params.put("key", mapKey);
    	params.put("locations", position.toString());
    	params.put("coordsys", "gps");
    	
    	String respStr = ChannelUtil.httpPost(path, ChannelUtil.getSortQueryString(params));
    	JSONObject resp = JSON.parseObject(respStr);
    	if(resp.getInteger("status") == 1){
    		return resp.getString("locations");
    	} else {
    		System.out.println("坐标转换" + resp.getString("info") + "，错误代码：" + resp.getInteger("status"));
    		return null;
    	}
    }
}
