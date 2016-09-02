package com.example.yanxu.newyongyou.model;

public class DistrictModel {
	private String name;
	private String zipcode;
	private String areaId ;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String name, String areaId, String zipcode) {
		super();
		this.name = name;
		this.areaId = areaId ;
		this.zipcode = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "DistrictModel [name=" + name + ", areaId=" + areaId + ", zipcode=" + zipcode + "]";
	}

}
