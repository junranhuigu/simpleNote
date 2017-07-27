package com.junranhuigu.simpleNote.vo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.junranhuigu.simpleNote.structure.Separator;

/**
 * 时间线信息——根据日期划分
 * */
public class TimeLine {
	private String year;//格式： yyyy年
	private String date;//格式： MM月dd日
	private List<TimeLineContent> contents;//详细信息
	
	public TimeLine() {
		// TODO Auto-generated constructor stub
	}
	
	public TimeLine(List<PhotoInfo> imgInfos) {
		Date _d = imgInfos.get(0).getTime();
		Calendar c = Calendar.getInstance();
		c.setTime(_d);
		this.year = c.get(Calendar.YEAR) + "年";
		this.date = (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日";
		Separator<String, PhotoInfo> separator = new Separator<String, PhotoInfo>() {
			@Override
			public String separate(PhotoInfo v) {
				return v.getAddress().getDetail();
			}
		};
		separator.addAll(imgInfos);
		Iterator<Entry<String, List<PhotoInfo>>> ite = separator.getResult().entrySet().iterator();
		this.contents = new ArrayList<>();
		while(ite.hasNext()){
			Entry<String, List<PhotoInfo>> entry = ite.next();
			String note = null;
			TimeLineContent content = new TimeLineContent(entry.getValue(), entry.getKey(), note);
			this.contents.add(content);
		}
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<TimeLineContent> getContents() {
		return contents;
	}
	public void setContents(List<TimeLineContent> contents) {
		this.contents = contents;
	}
}
