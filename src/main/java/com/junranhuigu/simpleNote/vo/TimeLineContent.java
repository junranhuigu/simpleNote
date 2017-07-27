package com.junranhuigu.simpleNote.vo;

import java.util.ArrayList;
import java.util.List;
/**
 * 根据地址划分
 * */
public class TimeLineContent {
	private String location;//地址
	private String locationDetail;//详细地址
	private List<String> imgPaths;//图片路径
	private String title;//标题
	private String weather;//天气信息
	private String note;//备注信息
	
	public TimeLineContent() {
		// TODO Auto-generated constructor stub
	}
	
	public TimeLineContent(List<PhotoInfo> imgInfos, String title, String note) {
		this.title = title;
		this.note = note;
		this.imgPaths = new ArrayList<>();
		this.locationDetail = imgInfos.get(0).getAddress().simpleDetail();
		this.location = imgInfos.get(0).getAddress().getDetail().replace(this.locationDetail, "");
		for(PhotoInfo info : imgInfos){
			this.imgPaths.add(info.getPath());
			this.weather = info.getWeather();
		}
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<String> getImgPaths() {
		return imgPaths;
	}
	public void setImgPaths(List<String> imgPaths) {
		this.imgPaths = imgPaths;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getLocationDetail() {
		return locationDetail;
	}
	public void setLocationDetail(String locationDetail) {
		this.locationDetail = locationDetail;
	}
}
