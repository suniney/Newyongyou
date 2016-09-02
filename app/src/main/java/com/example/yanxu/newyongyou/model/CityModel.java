package com.example.yanxu.newyongyou.model;

import java.util.List;

public class CityModel {
	private String name;
	private String cityId;
	private List<DistrictModel> districtList;

	public CityModel() {
		super();
	}

	public CityModel(String name, String cityId,
			List<DistrictModel> districtList) {
		super();
		this.name = name;
		this.cityId = cityId;
		this.districtList = districtList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public List<DistrictModel> getDistrictList() {
		return districtList;
	}

	public void setDistrictList(List<DistrictModel> districtList) {
		this.districtList = districtList;
	}

	@Override
	public String toString() {
		return "CityModel [name=" + name + ", cityId=" + cityId
				+ ", districtList=" + districtList + "]";
	}

}
