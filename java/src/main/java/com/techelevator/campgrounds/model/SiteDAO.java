package com.techelevator.campgrounds.model;

import java.util.List;

public interface SiteDAO {

	public Site getSiteById(Long id);

	public List<Site> getAllAvailibleCampsites(long campgroundID, String startDate, String endDate);


}
