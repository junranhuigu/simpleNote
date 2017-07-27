package com.junranhuigu.simpleNote.vo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.junranhuigu.simpleNote.util.MapUtil;
import com.junranhuigu.simpleNote.util.StringUtil;

public class PhotoInfo {
	private String path;
	private Position position;
	private Address address;
	private String weather;
	private Date time;
	private String equipment;//拍摄设备
	
	public PhotoInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public PhotoInfo(String path) {
		this.path = path;
		analysis();
	}
	
	private void analysis(){
		try {
			File file = new File(path);
			if(!file.exists()){
				return;
			}
			Metadata data = ImageMetadataReader.readMetadata(file);
			//收集日期
	    	List<Date> dates = new ArrayList<>();
	    	ExifIFD0Directory d1 = data.getFirstDirectoryOfType(ExifIFD0Directory.class);
	    	if(d1 != null){
	    		Date date = d1.getDate(ExifIFD0Directory.TAG_DATETIME, TimeZone.getDefault());
	    		if(date != null){
	    			dates.add(date);
	    		}
	    		//拍摄设备
				String make = d1.getString(ExifIFD0Directory.TAG_MAKE);
		    	String model = d1.getString(ExifIFD0Directory.TAG_MODEL);
		    	this.equipment = (StringUtil.isEmpty(make) ? "" : make) + "-" + (StringUtil.isEmpty(model) ? "" : model);
	    	}
			
			ExifSubIFDDirectory d3 = data.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
			if(d3 != null){
				Date date = d3.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED, TimeZone.getDefault());
				if(date != null){
					dates.add(date);
				}
				date = d3.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, TimeZone.getDefault());
				if(date != null){
					dates.add(date);
				}
			}
			//GPS定位信息
			GpsDirectory d2 = data.getFirstDirectoryOfType(GpsDirectory.class);
			
			if(d2 != null){
				this.position = new Position(d2.getDescription(GpsDirectory.TAG_LONGITUDE), d2.getDescription(GpsDirectory.TAG_LATITUDE));
				this.address = MapUtil.mapPosition(this.position);
			}
			
			//日期时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
			Date gpsDate = d2 != null ? sdf.parse(d2.getString(GpsDirectory.TAG_DATE_STAMP)) : null;
			this.time = filterDate(dates, gpsDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Date filterDate(List<Date> dates, Date gpsDate){
		if(gpsDate != null){//无GPS定位时间
			final Date _aim = gpsDate;
			Collections.sort(dates, new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					long remain1 = Math.abs(d1.getTime() - _aim.getTime());
					long remain2 = Math.abs(d2.getTime() - _aim.getTime());
					if(remain1 < remain2){
						return -1;
					} else if(remain1 > remain2){
						return 1;
					} else {
						return 0;
					}
				}
			});
		} else {
			Collections.sort(dates, new Comparator<Date>() {
				@Override
				public int compare(Date d1, Date d2) {
					long remain1 = d1.getTime();
					long remain2 = d2.getTime();
					if(remain1 < remain2){
						return -1;
					} else if(remain1 > remain2){
						return 1;
					} else {
						return 0;
					}
				}
			});
		}
		Date aimDate = dates.isEmpty() ? gpsDate : dates.get(0);
		return aimDate;
	}
	
	public String positionName(){
		if(this.address != null){
			return StringUtil.isEmpty(address.getCity()) || !StringUtil.hasChinese(address.getCity()) ? address.getProvince() : address.getCity();
		} else {
			return null;
		}
	}
	
	public String getPath() {
		return path;
	}
	public Position getPosition() {
		return position;
	}
	public Address getAddress() {
		return address;
	}
	public String getWeather() {
		return weather;
	}
	public Date getTime() {
		return time;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getEquipment() {
		return equipment;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	
}
