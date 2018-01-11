package com.junranhuigu.simpleNote.vo;

import com.junranhuigu.simpleNote.util.DateUtil;
import com.junranhuigu.simpleNote.util.WeatherUtil;

public class TimeLineContent {
	private String locationDetail;//详细地址
	private String imgPaths;//图片路径
	private String weather;//天气信息
	private String note;//备注信息
	private String date;//时间
	
	public TimeLineContent() {
		// TODO Auto-generated constructor stub
	}
	
	public TimeLineContent(PhotoInfo img, String note) {
		this.note = note;
		this.imgPaths = img.showPath();
		this.locationDetail = img.getAddress() != null ? img.getAddress().getDetail() : "";
		this.date = DateUtil.parseTime(img.getTime());
		this.weather = WeatherUtil.simpleWeather(img.getWeather());
	}
	
	public String getLocationDetail() {
		return locationDetail;
	}
	public void setLocationDetail(String locationDetail) {
		this.locationDetail = locationDetail;
	}
	public String getImgPaths() {
		return imgPaths;
	}
	public void setImgPaths(String imgPaths) {
		this.imgPaths = imgPaths;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
