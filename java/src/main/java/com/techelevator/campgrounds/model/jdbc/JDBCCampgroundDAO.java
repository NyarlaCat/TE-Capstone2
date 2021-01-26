package com.techelevator.campgrounds.model.jdbc;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campgrounds.model.Campground;
import com.techelevator.campgrounds.model.CampgroundDAO;
import com.techelevator.campgrounds.model.Park;


public class JDBCCampgroundDAO implements CampgroundDAO{

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	} 
	
	@Override  //tested
	public List<Campground> getAllCampgrounds() {
		ArrayList<Campground> campgrounds = new ArrayList<>();
		String sqlGetAllCampgrounds = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllCampgrounds);														
		while (results.next()) {																
			Campground theCampground = mapRowToCampground(results);
			campgrounds.add(theCampground);
		}
		return campgrounds;
	}
	
	@Override //tested
	public List<Campground> getCampgroundByName(String name) {
		ArrayList<Campground> campgrounds = new ArrayList<>();
		String sqlGetCampgroundByName = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground Where name =? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundByName, name);												
		while (results.next()) {																
			Campground theCampground = mapRowToCampground(results);
			campgrounds.add(theCampground);
		}
		return campgrounds;
		
	}

	@Override //tested
	public Campground getCampgroundById(Long Id) {
		String sqlGetCampgroundtById = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground Where campground_id =? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundtById, Id);
		
		if (results.next()) {
		Campground theCampground = mapRowToCampground(results);
		return theCampground;
		}
		return null;

	}
	@Override // tested
	public BigDecimal estimateReservationCost(long campgroundId, String startDate, String endDate) {
		String sqlGetDailyFee = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee From campground WHERE campground_id = ?";
		LocalDate start = null;
		LocalDate end = null;
		
		try {
			 start = LocalDate.parse(startDate);
		} catch (DateTimeParseException  e) {
			
		}
		try {
			end = LocalDate.parse(endDate);
		} catch (DateTimeParseException  e) {
			
		}
		
		double days = (double)ChronoUnit.DAYS.between(start, end) +1;
		
		Campground newCampground = new Campground();
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDailyFee, campgroundId);
		if (results.next()) {
			newCampground = mapRowToCampground(results);
		}
		BigDecimal cost = newCampground.getDailyfee().multiply(BigDecimal.valueOf(days));
		return cost;
		
	}


	@Override //tested
	public List<Campground> getAllCampgroundsByParkId(Long Id) {
		ArrayList<Campground> campgrounds = new ArrayList<>();
		String sqlGetCampgroundByParkId = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground Where park_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCampgroundByParkId, Id);														
		while (results.next()) {															
			Campground theCampground = mapRowToCampground(results);
			campgrounds.add(theCampground);
		}
		return campgrounds;	
	}
	
	private Campground mapRowToCampground(SqlRowSet results) {
				Campground theCampground;
				theCampground = new Campground();
				theCampground.setCampgroundId(results.getLong("campground_id"));
				theCampground.setParkId(results.getLong("park_id"));
				theCampground.setNameString(results.getString("name"));
				theCampground.setOpenDate(results.getString("open_from_mm"));
				theCampground.setCloseDate(results.getString("open_to_mm"));
				theCampground.setDailyfee(results.getBigDecimal("daily_fee"));

				return theCampground;

			}



	
		}