package com.techelevator.campgrounds.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campgrounds.model.Campground;
import com.techelevator.campgrounds.model.Park;
import com.techelevator.campgrounds.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO{
	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	} 

	@Override //Tested
	public List<Park> getAllParks() {
		ArrayList<Park> parks = new ArrayList<>();
		String sqlGetAllParks = "SELECT park_id, name, location, establish_date, area, visitors, description From park";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllParks);														
		while (results.next()) {																
			Park theparks = mapRowToPark(results);
			parks.add(theparks);
		}
		return parks;
	}

	@Override //tested
	public Park getParkById(Long id) {
		String sqlGetParktById = "SELECT park_id, name, location, establish_date, area, visitors, description From park Where park_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParktById, id);
		
		if (results.next()) {
		Park thePark = mapRowToPark(results);
		return thePark;
		}
		return null;

	}

	@Override //tested
	public List<Park> getParkByName(String parkName) {
		ArrayList<Park> parks = new ArrayList<>();
		String sqlGetParkByName = "SELECT park_id, name, location, establish_date, area, visitors, description From park Where name =? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetParkByName , parkName);														
		while (results.next()) {																
			Park theparks = mapRowToPark(results);
			parks.add(theparks);
		}
		return parks;
	}

	private Park mapRowToPark(SqlRowSet results) {
		Park thePark;
		thePark = new Park();
		thePark.setParkIdLong(results.getLong("park_id"));
		thePark.setName(results.getString("name"));
		thePark.setLocation(results.getString("location"));
		thePark.setEstablishedDate(results.getDate("establish_date"));
		thePark.setArea(results.getLong("area"));
		thePark.setVisitors(results.getLong("visitors"));
		thePark.setDescription(results.getString("description"));

		return thePark;

	}
}
