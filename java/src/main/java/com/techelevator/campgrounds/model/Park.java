package com.techelevator.campgrounds.model;

import java.sql.Date;

public class Park {

	private Long parkIdLong;
	private String name;
	private String location;
	private Date establishedDate;
	private Long area;
	private Long visitors;
	private String description;
	//Set
	
	public void setParkIdLong(Long parkIdLong) {
		this.parkIdLong = parkIdLong;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	//not tested
	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}
	public void setArea(Long area) {
		this.area = area;
	}
	public void setVisitors(Long visitors) {
		this.visitors = visitors;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	//get
	public Long getParkIdLong() {
		return parkIdLong;
	}
	public String getName() {
		return name;
	}
	public String getLocation() {
		return location;
	}
	
	//these getters not tested
	public Date getEstablishedDate() {
		return establishedDate;
	}
	public Long getArea() {
		return area;
	}
	public Long getVisitors() {
		return visitors;
	}
	public String getDescription() {
		return description;
	}
}
