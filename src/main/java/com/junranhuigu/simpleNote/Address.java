package com.junranhuigu.simpleNote;

import com.alibaba.fastjson.JSONObject;

public class Address {
	private String country;
	private String province;
	private String city;
	private String district;
	private String street;
	private String detail;//详细地址
	
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
	
	@Override
	public String toString() {
		return this.detail;
	}
}
