package com.techelevator.campgrounds.model;

import java.util.List;

public interface ParkDAO {

	public List<Park> getAllParks();
	public Park getParkById(Long id);
	public List<Park> getParkByName(String parkName);//list or single object? I don't think two nationl=al parks have the same name but what if?
	
}
