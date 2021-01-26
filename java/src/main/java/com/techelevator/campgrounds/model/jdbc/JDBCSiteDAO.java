package com.techelevator.campgrounds.model.jdbc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campgrounds.model.Campground;
import com.techelevator.campgrounds.model.Reservation;
import com.techelevator.campgrounds.model.Site;
import com.techelevator.campgrounds.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO{

	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	} 
	@Override //Tested
	public Site getSiteById(Long id) {
		String sqlGetSiteById = "Select site_id, campground_id ,site_number, max_occupancy, accessible, max_rv_length, utilities From site where site_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetSiteById, id);
		if (results.next()) {
			Site site = mapRowToSite(results);
			return site;
		}
		return null;
	}
	


	@Override //Tested
	public List<Site> getAllAvailibleCampsites(long campgroundId, String startDate, String endDate) {
		List<Site> availibleSiteList = new ArrayList<Site>();
		
		//data validation
		SimpleDateFormat dateStart = new SimpleDateFormat("yyyy-mm-dd");
		SimpleDateFormat dateEnd = new SimpleDateFormat("yyyy-mm-dd");
		try {
			dateStart.parse(startDate);
		} catch (ParseException nP) {
			System.out.println("Invalid date format entered for start date.");
		} 
		try {
			dateEnd.parse(endDate);
		} catch (ParseException  nP) {
			System.out.println("Invalid date format entered for end date.");
		} 
		String startMonth ="";
		String endMonth="";
		try {
			 startMonth = startDate.substring(5, 7);
		} catch (StringIndexOutOfBoundsException e) {
			// eat it bitch
		}
		try {
			endMonth = endDate.substring(5, 7);
		} catch (StringIndexOutOfBoundsException e) {
			// shove it in your pie hole java
		}
		//if the camp is in the list then put the camps in a list	
		
		String sqlGetAvalibleCampsites = 
				  "SELECT s.*, daily_fee "
				+ "FROM site s "
				+ "JOIN campground c ON s.campground_id = c.campground_id "
				+ "WHERE c.campground_id = ? AND c.open_from_mm <= ? AND c.open_to_mm >= ? "
				+ "AND s.site_id NOT IN(SELECT s.site_id "
									+ "FROM site s JOIN reservation r ON s.site_id = r.site_id "
									+ "WHERE ? BETWEEN r.from_date AND r.to_date OR"
									     + " ? BETWEEN r.from_date AND r.to_date)"
									+"LIMIT 5";
		LocalDate dateStartLocal = null;
		LocalDate dateEndLocal = null;
		try {
			 dateStartLocal = LocalDate.parse(startDate);
		} catch (DateTimeParseException e) {
			// just eat it
		} 
		try {
			dateEndLocal = LocalDate.parse(endDate);
		} catch (DateTimeParseException e) {
			// fuck it
		}
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAvalibleCampsites, campgroundId, startMonth, endMonth, dateStartLocal, dateEndLocal);
		
		//if all data is good map that shit to a list yo!
		try {
		while (results.next()) {
			Site s = mapRowToSite(results);
			availibleSiteList.add(s);
		}
		}catch (InvalidResultSetAccessException e) {
			// TODO: handle exception
		}
		return availibleSiteList;
	}

	
	
	
	
	private Site mapRowToSite(SqlRowSet results) {
		Site theSite;
		theSite = new Site();
		theSite.setSiteId(results.getLong("site_id"));
		theSite.setCampId(results.getLong("campground_id"));
		theSite.setSiteNumber(results.getInt("site_number"));
		theSite.setMaxOccupancy(results.getInt("max_occupancy"));
		theSite.setIsAccessible(results.getBoolean("accessible"));
		theSite.setMaxRvLength(results.getInt("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		return theSite;
	}

}
