package com.techelevator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campgrounds.model.Campground;
import com.techelevator.campgrounds.model.Park;
import com.techelevator.campgrounds.model.jdbc.JDBCCampgroundDAO;

public class CampgroundDAOIntegrationTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private static JdbcTemplate jdbcTemplate;
	private JDBCCampgroundDAO campgroundDAO;

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
		jdbcTemplate = new JdbcTemplate(dataSource);
		dataSource.setAutoCommit(false);
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
	public void instancisatJDBCSiteDAO() {
	campgroundDAO = new JDBCCampgroundDAO(dataSource);
	}
	
	//getCampgroundById(Long Id)
	@Test
	public void get_all_campgrounds_by_id_returns_the_expected_campground() {
		Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
		long parkId= newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("Holey Moley", parkId);
		
		
		Long expectedCampgroundId = newCampground.getCampgroundId();
		final String expectedName = "Holey Moley";
		Campground compareThisCampground = campgroundDAO.getCampgroundById(expectedCampgroundId);
		String nameReturnedByMethod = compareThisCampground.getNameString();
		
		Long campgroundIdReturnedByMethod = compareThisCampground.getCampgroundId();
		
		Assert.assertEquals("Name not as expected", expectedName, nameReturnedByMethod);
		Assert.assertEquals("Id returned is not as expected", expectedCampgroundId, campgroundIdReturnedByMethod);
	}
	
	
	//getAllCampgroundsByParkId(Long Id)
	@Test
	public void get_all_campgrounds_associated_with_a_specific_park_by_id() {
		Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
		long parkId= newPark.getParkIdLong();
		
	 Campground newCampground = makeFakeCampgroundInTheDatabase("Kelp",parkId);
	 Campground newCampground1 = makeFakeCampgroundInTheDatabase("lol",parkId);
	 Campground newCampground2 = makeFakeCampgroundInTheDatabase("Oh No",parkId);
	 

	 List <Campground> campList = campgroundDAO.getAllCampgroundsByParkId(parkId);
	 
	 int actualListSize = campList.size();
	 final int expectedListSize = 3;
	 
	 Assert.assertEquals(expectedListSize, actualListSize);
	}
	
	//getCampgroundByName(String name)
	@Test
	public void get_campground_by_name_returns_campgrounds_of_that_name() {
		Park newPark = makeFakeParkInTheDatabase("Pool slope", "Main");
		long parkId = newPark.getParkIdLong();
		Campground newCampground = makeFakeCampgroundInTheDatabase("skjdnvkljsdnzxc", parkId);
		Park newPark1 = makeFakeParkInTheDatabase("Somewhere", "Vermont");
		long parkId1 = newPark1.getParkIdLong();
		makeFakeCampgroundInTheDatabase("skjdnvkljsdnzxc", parkId1);
		
		List<Campground> campgroundList= campgroundDAO.getCampgroundByName("skjdnvkljsdnzxc");
		int actualListSize = campgroundList.size();
		final int expectedListSize = 2;
		
		Assert.assertEquals(expectedListSize, actualListSize);
		
	}
	
	//getAllCampgrounds()
	@Test
	public void get_all_campgrounds_gets_all_campgrounds() {
		List<Campground> campgoundList = campgroundDAO.getAllCampgrounds();
		int initialListSize = campgoundList.size();
		 
		Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
		long parkId= newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("Kelp",parkId);
		Campground newCampground1 = makeFakeCampgroundInTheDatabase("lol",parkId);
		Campground newCampground2 = makeFakeCampgroundInTheDatabase("Oh No",parkId);
		
		List<Campground> campgoundList1 = campgroundDAO.getAllCampgrounds();
		int actualListSize = campgoundList1.size();
		final int expectedListSize = initialListSize + 3;
		
		Assert.assertEquals(expectedListSize, actualListSize);	
	}
	
	@Test
	//public Campground getCampgroundDailyCost(long campgroundId) {
	public void get_daily_cost_returns_the_cost_for_the_campground() {
		 
		Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
		long parkId= newPark.getParkIdLong();
		Campground newCampground = makeFakeCampgroundInTheDatabase("Kelp",parkId);
		
		newCampground.setDailyfee(BigDecimal.valueOf(50.00));
		final BigDecimal expectedFee = BigDecimal.valueOf(50.00);
		BigDecimal actualFee = newCampground.getDailyfee();
		
		Assert.assertEquals(expectedFee, actualFee);
	}
		
	@Test	 
	 public void reservation_esimator_works() {
		Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
		long parkId= newPark.getParkIdLong();
		Campground newCampground = makeFakeCampgroundInTheDatabase("Kelp",parkId);
		long campgroundId = newCampground.getCampgroundId();
		
		
		double daysActual = 2;
		
		BigDecimal dailyFeeCoded = BigDecimal.valueOf(35.00);
		BigDecimal dailyFeeActual = newCampground.getDailyfee();
		Assert.assertEquals("daily fee is not as ecpected", dailyFeeCoded, dailyFeeActual);
		
		BigDecimal expectedFee = dailyFeeActual.multiply(BigDecimal.valueOf(daysActual));
		BigDecimal actualFeeFromMethod = campgroundDAO.estimateReservationCost(campgroundId, "2017-09-01", "2017-09-02");
		
		Assert.assertEquals("Method is not returning the cost that's expected", expectedFee, actualFeeFromMethod);
		
		
	}
	 
	 
	//Helper method to make fake campgrounds and insert it into the database. Will also need to make a fake park to assign it to
	private Campground makeFakeCampgroundInTheDatabase(String name, long parkId) {
		Campground newCampground = new Campground();
		
		newCampground.setParkId(parkId);
		newCampground.setNameString(name);
		newCampground.setDailyfee(BigDecimal.valueOf(35.00));
		newCampground.setCloseDate("11");
		newCampground.setOpenDate("02");
		
		String sqlInsertCampground = "INSERT INTO campground (park_id,name, open_from_mm, open_to_mm, daily_fee) VALUES(?,?,?,?,?) RETURNING campground_id";
		
		long campgroundID = jdbcTemplate.queryForObject(sqlInsertCampground, Long.TYPE, newCampground.getParkId(), newCampground.getNameString(),newCampground.getOpenDate(),newCampground.getCloseDate(),newCampground.getDailyfee());
		
		
		newCampground.setCampgroundId(campgroundID);
		return newCampground;
		
	}
	
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
