package com.techelevator.campgrounds.model;

public class Site {

	private Long campId;
	private int siteNumber;
	private int maxOccupancy;
	private Boolean isAccessible;
	private int maxRvLength;
	private Boolean utilities;
	private Long siteId;
	
	//Set
	public void setCampId(Long campId) {
		this.campId = campId;
	}
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	public void setIsAccessible(Boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}
	public void setUtilities(Boolean utilities) {
		this.utilities = utilities;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	
	
	//Get
	public Long getSiteId() {
		return siteId;
	}
	public Long getCampId() {
		return campId;
	}
	public int getSiteNumber() {
		return siteNumber;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public Boolean getIsAccessible() {
		return isAccessible;
	}
	public int getMaxRvLength() {
		return maxRvLength;
	}
	public Boolean getUtilities() {
		return utilities;
	}
}
