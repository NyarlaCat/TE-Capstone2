package com.techelevator.campgrounds.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Campground {
	
	private Long parkId;
	private String nameString;
	private String close;
	private String open;
	private BigDecimal dailyfee;
	private Long campgroundId;
	
	
	//Set
	public void setCampgroundId(Long id) {
		this.campgroundId = id ;
	}
	
	
	public void setParkId(Long parkId) {
		this.parkId = parkId;
	}

	public void setNameString(String nameString) {
		this.nameString = nameString;
	}

	public void setOpenDate(String open) {
		this.open = open;
	}

	public void setCloseDate(String close) {
		this.close = close;
	}

	public void setDailyfee(BigDecimal dailyfee) {
		this.dailyfee = dailyfee;
	}
	
	public String getOpenAsMonth() {
		String openDate = getOpenDate();
		openDate = convertToMonth(openDate);
		return openDate;
	}
	
	public String getCloseAsMonth() {
		String closeDate = getCloseDate();
		closeDate = convertToMonth(closeDate);
		return closeDate;
	}
	
	//Get
	public Long getCampgroundId() {
		return campgroundId;
	}

	public String getNameString() {
		return nameString;
	}
	public String getCloseDate() {
		return close;
	}
	public String getOpenDate() {
		return open;
	}
	public BigDecimal getDailyfee() {
		return dailyfee;
	}
	public Long getParkId() {
		return parkId;
	}
	
	//we're not ever making a new campground to save to the data base so why should it matter to save the data as something useful only to this application?
	// for dealing with closing months being listed as number strings
	private String convertToMonth(String month) {
		Integer monthNum = Integer.parseInt(month);
		String monthString = "";
		if (monthNum == 12) {
			monthString = "December";
		} else if (monthNum == 11) {
			monthString = "November";
		}else if (monthNum == 10) {
			monthString = "October";
		}else if (monthNum == 9) {
			monthString = "September";
		}else if (monthNum == 8) {
			monthString = "August";
		}else if (monthNum == 7) {
			monthString = "July";
		}else if (monthNum == 6) {
			monthString = "June";
		}else if (monthNum == 5) {
			monthString = "May";
		}else if (monthNum == 4) {
			monthString = "April";
		}else if (monthNum == 3) {
			monthString = "March";
		}else if (monthNum == 2) {
			monthString = "February";
		}else if (monthNum == 1) {
			monthString = "January";
		}
		return monthString;	
		
		
	}

}