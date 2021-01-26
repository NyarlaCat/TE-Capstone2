package unit_tests;

import org.junit.Assert;
import org.junit.Test;

import com.techelevator.campgrounds.model.Site;

public class SiteTest {
	
	@Test
	public void test_get_and_set_campId() {
		Site newSite = new Site();
		newSite.setCampId(Long.valueOf(2909));
		
		long actual = newSite.getCampId();
		final long expected = Long.valueOf(2909);
		
		Assert.assertEquals(expected, actual);
		
	}
	
	@Test
	public void test_get_and_set_siteNumber() {
		Site newSite = new Site();
		newSite.setSiteNumber(12);
		
		int actual = newSite.getSiteNumber();
		final int expected = 12;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_get_and_set_maxOccupancy() {
		Site newSite = new Site();
		newSite.setMaxOccupancy(10);
		
		int actual = newSite.getMaxOccupancy();
		final int expected = 10;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_get_and_set_isAccessible() {
		Site newSite = new Site();
		newSite.setIsAccessible(true);
		
		boolean actual = newSite.getIsAccessible();
		final boolean expected = true;
		
		Assert.assertEquals(expected, actual);	
	}
	
	
	@Test
	public void test_get_and_set_rvLength() {
		Site newSite = new Site();
		newSite.setMaxRvLength(50);
		
		int actual = newSite.getMaxRvLength();
		final int expected = 50;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_get_and_set_utilities() {
		Site newSite = new Site();
		newSite.setUtilities(false);
		
		boolean actual = newSite.getUtilities();
		final boolean expected = false;
		
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void test_get_and_set_siteId() {
		Site newSite = new Site();
		newSite.setSiteId(Long.valueOf(345));
		
		long actual = newSite.getSiteId();
		final long expected = Long.valueOf(345);
		
		Assert.assertEquals(expected, actual);
	}
}
