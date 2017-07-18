package com.junranhuigu.simpleNote.vo;

import com.alibaba.fastjson.JSONObject;

public class Address {
	private String country;
	private String province;
	private String city;
	private String district;
	private String street;
	private String detail;//详细地址
	private String mapLocations;//高德地图坐标
	
	public Address(JSONObject obj) {
		this.country = obj.getJSONObject("addressComponent").getString("country");
		this.province = obj.getJSONObject("addressComponent").getString("province");
		this.city = obj.getJSONObject("addressComponent").getString("city");
		this.district = obj.getJSONObject("addressComponent").getString("district");
		this.street = obj.getJSONObject("addressComponent").getString("township");
		this.detail = obj.getString("formatted_address");
	}
	
	public String getCountry() {
		return country;
	}
	public String getProvince() {
		return province;
	}
	public String getCity() {
		return city;
	}
	public String getDistrict() {
		return district;
	}
	public String getStreet() {
		return street;
	}
	public String getDetail() {
		return detail;
	}
	public String getMapLocations() {
		return mapLocations;
	}
	public void setMapLocations(String mapLocations) {
		this.mapLocations = mapLocations;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Override
	public String toString() {
		return this.detail;
	}
}
