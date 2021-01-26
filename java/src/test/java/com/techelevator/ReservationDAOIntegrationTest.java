package com.techelevator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campgrounds.model.Campground;
import com.techelevator.campgrounds.model.Park;
import com.techelevator.campgrounds.model.Reservation;
import com.techelevator.campgrounds.model.Site;
import com.techelevator.campgrounds.model.jdbc.JDBCReservationDAO;



public class ReservationDAOIntegrationTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private static JdbcTemplate jdbcTemplate;
	private JDBCReservationDAO reservationDAO;

	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	//Instantiate the object for testing
	@Before
	public void instancisatJDBCReservationDAO() {
		reservationDAO = new JDBCReservationDAO(dataSource);
	}
	@Test
	public void calculating_the_cost_should_fucking_work_right_goddamn_it() {
		Park newPark = makeFakeParkInTheDatabase("121njsnfskjnk", "Ohio");
		long parkId = newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("Lazy", parkId);
		long campgroundId = newCampground.getCampgroundId();
		
		Site newSite = makeFakeSiteInTheDatabase(campgroundId, 1);
		long siteId = newSite.getSiteId();
		 
		Reservation newReservation = makeFakeReservationInTheDataBase(LocalDate.of(2019, 05, 01), LocalDate.of(2019, 05, 03), "23423423", siteId);
		long reservationId = newReservation.getReservationId();
		
		double daysActual = (double)ChronoUnit.DAYS.between(newReservation.getFromDate(),newReservation.getToDate()) + 1;
		double daysExpected = 3;
		Assert.assertEquals("day count is not as expected", daysExpected, daysActual, .00001);
		
		BigDecimal dailyFeeCoded = BigDecimal.valueOf(35.00);
		BigDecimal dailyFeeActual = newCampground.getDailyfee();
		Assert.assertEquals("daily fee is not as ecpected", dailyFeeCoded, dailyFeeActual);
		
		
		BigDecimal expectedFee = dailyFeeActual.multiply(BigDecimal.valueOf(daysActual));
		BigDecimal actualFeeFromMethod = reservationDAO.calculateReservationCost(reservationId);
		Assert.assertEquals("Method is not returning the esults expected", expectedFee, actualFeeFromMethod);
	}
	
	
	
	
	@Test
	public void get_reservation_by_id_retruns_the_expected_reservation() {
		Park newPark = makeFakeParkInTheDatabase("121njsnfskjnk", "Ohio");
		long parkId = newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("8uniomo", parkId);
		long campgroundId = newCampground.getCampgroundId();
		
		Site newSite = makeFakeSiteInTheDatabase(campgroundId, 1);
		long siteId = newSite.getSiteId();
		
		Reservation newReservation = makeFakeReservationInTheDataBase(LocalDate.of(2019, 05, 01), LocalDate.of(2019, 05, 03), "23423423", siteId);
		long reservationId = newReservation.getReservationId();
		
		Reservation compareReservation =  reservationDAO.getReservationById(reservationId);
		String actualName = compareReservation.getName();
		final String expectedName = "23423423";
		
		Assert.assertEquals(expectedName, actualName);
	}
	
	@Test
	public void make_a_reservation_works_and_makes_a_reservation() {
		Park newPark = makeFakeParkInTheDatabase("121njsnfskjnk", "Ohio");
		long parkId = newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("8uniomo", parkId);
		long campgroundId = newCampground.getCampgroundId();
		
		Site newSite = makeFakeSiteInTheDatabase(campgroundId, 1);
		
		
		Reservation newReservation = reservationDAO.makeReservation(Long.valueOf(1), "Julia", "2019-09-20", "2019-09-22");
		
		long siteIdActual = newReservation.getSiteId();
		long siteIdExpected = Long.valueOf(1);
		
		String actualName = newReservation.getName();
		final String expectedName = "Julia";
		
		Assert.assertEquals("Reservation name is not as expected" , expectedName, actualName);
		Assert.assertEquals("Reservation site id is not as expected" , siteIdExpected, siteIdActual);
		
	}
	
	@Test
	
	public void create_reservation_takes_a_mostly_set_reservation_object_finishes_setting_its_vales_then_inserts_it_into_the_data_base() {
		Park newPark = makeFakeParkInTheDatabase("121njsnfskjnk", "Ohio");
		long parkId = newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("8uniomo", parkId);
		long campgroundId = newCampground.getCampgroundId();
		
		Site newSite = makeFakeSiteInTheDatabase(campgroundId, 1);
		long siteId = newSite.getSiteId();
		
		Reservation newReservation = makeFakeReservationInTheDataBase(LocalDate.of(2019, 05, 01), LocalDate.of(2019, 05, 03), "Julia Rose Brooks and Family", siteId);
		long reservationId = newReservation.getReservationId();
		
		reservationDAO.createNewReservation(newReservation);
		
		String actualName = newReservation.getName();
		final String expectedName = "Julia Rose Brooks and Family";
		Assert.assertEquals("Reservation name is not as expected" , expectedName, actualName);
		
		long actualReservationId = newReservation.getReservationId();
		Assert.assertNotNull(actualReservationId);
		
		
		long actualSiteId = newReservation.getSiteId();
		Assert.assertEquals("Reservation site id is not as expected" , siteId, actualSiteId);
		
	}
	
	

	
	
	
	
	
	
	
	//HELPERS
	
	private Reservation makeFakeReservationInTheDataBase(LocalDate startDate, LocalDate endDate, String name, long siteId) {
		Reservation newReservation = new Reservation();
	
		newReservation.setFromDate(startDate);
		newReservation.setToDate(endDate);
		newReservation.setSiteId(siteId);
		newReservation.setName(name);
		
		String sqlInsertReservation = "INSERT INTO reservation (name, site_id, from_date, to_date, create_date) "
							 + "VALUES(?,?,?,?,'2019-10-27') RETURNING reservation_id";
		
		long reservationId = jdbcTemplate.queryForObject(sqlInsertReservation, Long.TYPE, newReservation.getName(),newReservation.getSiteId(),newReservation.getFromDate(),newReservation.getToDate() );
		
		newReservation.setReservationId(reservationId);
		
		
		return newReservation;
		
	}
	
	
	
	//Helper make a fake site
	private Site makeFakeSiteInTheDatabase(long campgroundId, int siteNumber) {
		Site newSite = new Site();
		
		newSite.setSiteNumber(siteNumber);
		newSite.setCampId(campgroundId);
		newSite.setMaxOccupancy(10);
		newSite.setIsAccessible(false);
		newSite.setMaxRvLength(20);
		newSite.setUtilities(true);
		
		
		String sqlInsertSite = "INSERT INTO site (utilities, site_number, max_rv_length, max_occupancy, campground_id, accessible) "
							 + "VALUES(?,?,?,?,?,?) RETURNING site_id";
		
		long siteId = jdbcTemplate.queryForObject(sqlInsertSite, Long.TYPE, newSite.getUtilities(),newSite.getSiteNumber(),newSite.getMaxRvLength(),newSite.getMaxOccupancy(),newSite.getCampId(),newSite.getIsAccessible());
		
		newSite.setSiteId(siteId);
		return newSite;
	}
	
	//helper to make fake campsite
	private Campground makeFakeCampgroundInTheDatabase(String name, long parkId) {
		Campground newCampground = new Campground();
		
		newCampground.setParkId(parkId);
		newCampground.setNameString(name);
		newCampground.setDailyfee(BigDecimal.valueOf(35.00));
		newCampground.setCloseDate("12");
		newCampground.setOpenDate("01");
		
		String sqlInsertCampground = "INSERT INTO campground (park_id,name, open_from_mm, open_to_mm, daily_fee) VALUES(?,?,?,?,?) RETURNING campground_id";
		
		long campgroundID = jdbcTemplate.queryForObject(sqlInsertCampground, Long.TYPE, newCampground.getParkId(), newCampground.getNameString(),newCampground.getOpenDate(),newCampground.getCloseDate(),newCampground.getDailyfee());
		
		
		newCampground.setCampgroundId(campgroundID);
		return newCampground;
		
	}
	
	//helper to make fake park
	private Park makeFakeParkInTheDatabase(String name, String location) {
		Park newPark = new Park();
		newPark.setName(name);
		newPark.setLocation(location);
		
		String sqlInsertPark = "INSERT INTO park ( name, location, establish_date, area, visitors, description) VALUES (?,?, '1900-09-01', 45000, 60000, 'description is here and its amazing') RETURNING park_id";
		long parkId = jdbcTemplate.queryForObject(sqlInsertPark, Long.TYPE, newPark.getName(), newPark.getLocation());
		newPark.setParkIdLong(parkId);
		
		return newPark;
	}
}
