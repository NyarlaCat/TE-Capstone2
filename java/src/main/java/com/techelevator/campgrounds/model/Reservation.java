package com.techelevator.campgrounds.model;

import java.time.LocalDate;

public class Reservation {
	private Long siteId;
	private String name;
	private LocalDate fromDate;
	private LocalDate toDate;
	private LocalDate createDate;
	private Long reservationId;
	
	//set
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	public void setReservationId(Long reservationId) {
		this.reservationId = reservationId;
	}
	
	//Get
	public Long getReservationId() {
		return reservationId;
	}
	public Long getSiteId() {
		return siteId;
	}
	public String getName() {
		return name;
	}
	public LocalDate getFromDate() {
		return fromDate;
	}
	public LocalDate getToDate() {
		return toDate;
	}
	
	//not tested
	public LocalDate getCreateDate() {
		return createDate;
	}
	

}
