package com.junranhuigu.simpleNote.vo;

public class Position {
	private float longtitude;
	private float latitude;
	
	public Position() {
		// TODO Auto-generated constructor stub
	}
	
	public Position(String longtitude, String latitude) {
		longtitude = longtitude.replace(" ", "");
		latitude = latitude.replace(" ", "");
		this.longtitude = Integer.parseInt(longtitude.substring(0, longtitude.indexOf("°"))) + Integer.parseInt(longtitude.substring(longtitude.indexOf("°") + 1, longtitude.indexOf("'"))) / 60.0f + Float.parseFloat(longtitude.substring(longtitude.indexOf("'") + 1, longtitude.indexOf("\""))) / 3600.0f;
		this.latitude = Integer.parseInt(latitude.substring(0, latitude.indexOf("°"))) + Integer.parseInt(latitude.substring(latitude.indexOf("°") + 1, latitude.indexOf("'"))) / 60.0f + Float.parseFloat(latitude.substring(latitude.indexOf("'") + 1, latitude.indexOf("\""))) / 3600.0f;
	}
	
	public float getLongtitude() {
		return longtitude;
	}
	public float getLatitude() {
		return latitude;
	}
	
	public void setLongtitude(float longtitude) {
		this.longtitude = longtitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return longtitude + "," + latitude;
	}
}
