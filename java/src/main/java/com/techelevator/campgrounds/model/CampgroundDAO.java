package com.techelevator.campgrounds.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.ObjLongConsumer;



public interface CampgroundDAO {

	public List<Campground> getAllCampgrounds();
	
	public List<Campground> getCampgroundByName(String name);
	
	public Campground getCampgroundById(Long Id);
	
	public BigDecimal estimateReservationCost(long campgroundId, String startDate, String endDate);
	
	public List<Campground> getAllCampgroundsByParkId(Long Id);
	
	
}