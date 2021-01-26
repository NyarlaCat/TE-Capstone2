package com.techelevator;

import java.math.BigDecimal;
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
import com.techelevator.campgrounds.model.Site;
import com.techelevator.campgrounds.model.jdbc.JDBCSiteDAO;

public class SiteDAOIntegrationTest {

	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private static JdbcTemplate jdbcTemplate;
	private JDBCSiteDAO siteDAO;

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
	public void instancisatJDBCSiteDAO() {
		siteDAO = new JDBCSiteDAO(dataSource);
	}
	
 
	@Test
	public void get_list_of_avalible_sites_dosent_break_and_returns_a_list() {
		Park newPark = makeFakeParkInTheDatabase("121njsnfskjnk", "Ohio");
		long parkId = newPark.getParkIdLong();
		Campground newCampground = makeFakeCampgroundInTheDatabase("Lazy", parkId);
		long campgroundId = newCampground.getCampgroundId();
		Site newSite = makeFakeSiteInTheDatabase(campgroundId, 1);
		Site newSite1 = makeFakeSiteInTheDatabase(campgroundId, 2);
		Site newSite2 = makeFakeSiteInTheDatabase(campgroundId, 3);
		
		List<Site> siteList = siteDAO.getAllAvailibleCampsites(campgroundId, "2019-06-01", "2019-06-20");
		
		int actualSiteListSize = siteList.size();
		final int expectedSiteListSize = 3;
		
		Assert.assertEquals(expectedSiteListSize, actualSiteListSize);	
	}
	
	@Test
	public void this_test_searches_a_campground_during_its_closed_seson_and_will_put_it_in_the_list_if_it_is() {
		Park newPark = makeFakeParkInTheDatabase("121njsnfskjnk", "Ohio");
		long parkId = newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("Lazy", parkId);
		long campgroundId = newCampground.getCampgroundId();
		
		Site newSite = makeFakeSiteInTheDatabase(campgroundId, 1);
		Site newSite1 = makeFakeSiteInTheDatabase(campgroundId, 2);
		Site newSite2 = makeFakeSiteInTheDatabase(campgroundId, 3);
		
		List<Site> siteList = siteDAO.getAllAvailibleCampsites(campgroundId, "2019-01-01", "2019-01-20");
		
		int actualSiteListSize = siteList.size();
		final int expectedSiteListSize = 0;
		
		Assert.assertEquals(expectedSiteListSize, actualSiteListSize);	
		
	}
	// Site getSiteById(Long id);
	@Test
	public void get_site_by_id_works_to_resturn_the_correct_site() {
		Park newPark = makeFakeParkInTheDatabase("121njsnfskjnk", "Ohio");
		long parkId = newPark.getParkIdLong();
		
		Campground newCampground = makeFakeCampgroundInTheDatabase("Lazy", parkId);
		long campgroundId = newCampground.getCampgroundId();
		
		Site newSite = makeFakeSiteInTheDatabase(campgroundId, 19);
		long siteId= newSite.getSiteId();
		
		Site compareThisSite = siteDAO.getSiteById(siteId);
		
		int actualSiteNumber = compareThisSite.getSiteNumber();
		final int expectedSiteNumber = 19;
		
		long actualCampgroundId = compareThisSite.getCampId();
		
		Assert.assertEquals("site number is not as expected", expectedSiteNumber, actualSiteNumber);
		Assert.assertEquals("campground I is not as expected", campgroundId, actualCampgroundId);
		
	}
	
	//helper to make a fake site
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
		newCampground.setCloseDate("09");
		newCampground.setOpenDate("03");
		
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
