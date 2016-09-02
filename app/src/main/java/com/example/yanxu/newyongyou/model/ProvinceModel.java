package com.example.yanxu.newyongyou.model;

import java.util.List;

public class ProvinceModel {
	private String name;
	/**
	 * 
	 */
	private String provId ;
	private List<CityModel> cityList;
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String name, String provId ,List<CityModel> cityList) {
		super();
		this.name = name;
		this.provId = provId;
		this.cityList = cityList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getProvId() {
		return provId;
	}

	public void setProvId(String provId) {
		this.provId = provId;
	}

	public List<CityModel> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [name=" + name + ", provId=" + provId + ", cityList=" + cityList + "]";
	}
	
}
