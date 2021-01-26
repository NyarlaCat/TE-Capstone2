package com.techelevator.campgrounds.model.jdbc;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.sql.DataSource;

import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campgrounds.model.Campground;
import com.techelevator.campgrounds.model.Reservation;
import com.techelevator.campgrounds.model.ReservationDAO;
import com.techelevator.campgrounds.model.Site;

public class JDBCReservationDAO implements ReservationDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override //Tested
	public Reservation getReservationById(Long id) {
		String sqlGetReservationById = "Select reservation_id ,site_id, name ,from_date ,to_date, create_date from reservation where reservation_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetReservationById, id);
		if (results.next()) {
			Reservation reservation = mapRowToReservation(results);
			return reservation;
		} 
		return null;
	}

	@Override //WE NEED TO DO THIS ONE SO THAT THE WILDCARD SEARCH WORKS not actually a requirement go figure //NOT TESTED
	public Reservation getReservationByName(String name) {
		
		String sqlReservationByName = "Select reservation_id,site_id,name,from_date,to_date,create_date  ,from_date ,to_date, create_date from reservation where name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlReservationByName, name);
		if (results.next()) {
			Reservation theReservation = mapRowToReservation(results);
			return theReservation;
		}
		return null;
	} 


	@Override  //TESTED
	public BigDecimal calculateReservationCost(long reservationId) {
		BigDecimal dailyCost = BigDecimal.ZERO;
		Reservation reservation = getReservationById(reservationId);
		double days = (double)ChronoUnit.DAYS.between(reservation.getFromDate(),reservation.getToDate()) +1;
		long siteId = reservation.getSiteId();
		
		String sqlGetDailyCost = "SELECT c.daily_fee "
							   + "FROM reservation r "
							   + "JOIN site s On s.site_id = r.site_id "
							   + "JOIN campground c ON c.campground_id = s.campground_id "
							   + "WHERE r.reservation_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetDailyCost, reservationId);
		while (results.next()) {
			dailyCost = results.getBigDecimal(1);
		}
		
		BigDecimal totalCost = dailyCost.multiply(BigDecimal.valueOf(days));
		return totalCost;
	}

	@Override //TESTED!
	public  Reservation makeReservation(Long siteId, String name, String startDate, String endDate) {
		Reservation newReservation = new Reservation();
		String sqlInsertReservation = "INSERT INTO reservation (name, site_id, from_date, to_date, create_date) "
				 + "VALUES(?,?,?,?,?) RETURNING reservation_id";

		long reservationId = jdbcTemplate.queryForObject(sqlInsertReservation, Long.TYPE, name, siteId, LocalDate.parse(startDate), LocalDate.parse(endDate), LocalDate.now());
		
		return newReservation = getReservationById(reservationId);

	}
	

	private Reservation mapRowToReservation(SqlRowSet results) {
		Reservation theReservation;
		theReservation = new Reservation();
		theReservation.setReservationId(results.getLong("reservation_id"));
		theReservation.setSiteId(results.getLong("site_id"));
		theReservation.setName(results.getString("name"));
		try {
		theReservation.setFromDate(results.getDate("from_date").toLocalDate());
		}catch(NullPointerException e) {
			theReservation.setFromDate(null);
		}
		try {
		theReservation.setToDate(results.getDate("to_date").toLocalDate());
		} catch (NullPointerException e) {
			theReservation.setToDate(null);
		}
		try {
		theReservation.setCreateDate(results.getDate("create_date").toLocalDate());
		}catch(NullPointerException e) {
			theReservation.setFromDate(null);
		}
		return theReservation;
	}

	@Override //Tested
	public Reservation createNewReservation(Reservation newReservation) {
		
		String sqlInsertReservation = "INSERT INTO reservation (name, site_id, from_date, to_date, create_date) "
				 + "VALUES(?,?,?,?,?) RETURNING reservation_id";

		long reservationId = jdbcTemplate.queryForObject(sqlInsertReservation, Long.TYPE, newReservation.getName(), newReservation.getSiteId(), newReservation.getFromDate(),newReservation.getToDate(), LocalDate.now());
		
		newReservation.setReservationId(reservationId);		
		return newReservation;
	}





}





