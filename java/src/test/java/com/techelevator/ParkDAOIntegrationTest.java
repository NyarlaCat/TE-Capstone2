package com.techelevator;

import java.sql.SQLException;
import java.util.List;

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
import com.techelevator.campgrounds.model.jdbc.JDBCParkDAO;

public class ParkDAOIntegrationTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private static JdbcTemplate jdbcTemplate;
	private JDBCParkDAO parkDAO;

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
	parkDAO = new JDBCParkDAO(dataSource);
	}
	

	//public List<Park> getAllParks();
	@Test
	public void get_all_parks_returns_a_list_of_all_parks_in_the_data_base() {
	List<Park> parkList = parkDAO.getAllParks();
	int initialListSize = parkList.size();
	 
	Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
	Park newPark1 = makeFakeParkInTheDatabase("lopk", "Texas");
	Park newPark2 = makeFakeParkInTheDatabase("lopk", "Texas");

	List<Park> parkList1 = parkDAO.getAllParks();
	int actualListSize = parkList1.size();
	final int expectedListSize = initialListSize + 3;
	
	Assert.assertEquals(expectedListSize, actualListSize);	
	}
	
	//public Park getParkById(Long id);
	@Test
	public void get_park_associated_with_a_specific_park_id() {
		
		Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
		long parkId = newPark.getParkIdLong();
		
		Park compareThisPark = parkDAO.getParkById(parkId);
		
		String actualName = compareThisPark.getName();
		final String expectedName = newPark.getName();
		
		Assert.assertEquals(expectedName, actualName);
	}
	
	//public List<Park> getParkByName(String parkName);
	@Test
	public void get_park_by_name_returns_a_list_of_all_parks_that_have_the_same_name() {
		Park newPark = makeFakeParkInTheDatabase("lopk", "Texas");
		Park newPark1 = makeFakeParkInTheDatabase("lopk", "Ohio");
		Park newPark2 = makeFakeParkInTheDatabase("lopk", "Hawaii");
		Park newPark3 = makeFakeParkInTheDatabase("lopk", "Maine");
		
		List <Park> parkList = parkDAO.getParkByName("lopk");
		
		int actualListSize = parkList.size();
		final int expectedListSize = 4;
		
		Assert.assertEquals(expectedListSize, actualListSize);
		
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
