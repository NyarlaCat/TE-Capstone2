package unit_tests;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

import com.techelevator.campgrounds.model.Campground;

public class CampgroundTest {

	
	@Test
	public void campground_convert_month_works() {
		Campground newCampground = new Campground();
		newCampground.setOpenDate("05");
		newCampground.setCloseDate("09");
		
		String actualOpenMonth = newCampground.getOpenAsMonth();
		String actualCloseMonth = newCampground.getCloseAsMonth();
		
		final String expectedOpenMonth = "May";
		final String expctedCloseMonth = "September";
		
		Assert.assertEquals("Open month is not as expcted ", expectedOpenMonth, actualOpenMonth);
		Assert.assertEquals("Close month is not as expectd", expctedCloseMonth, actualCloseMonth);
	}
	
	@Test
	public void campground_set_and_get_park_id() {
		Campground newCampground = new Campground();	
		newCampground.setCampgroundId(Long.valueOf(987));	
		long actualId = newCampground.getCampgroundId();
		final long expectedId = Long.valueOf(987);
		Assert.assertEquals(expectedId,actualId);
		
	}

	@Test
	public void campground_set_and_get_name() {
		Campground newCampground = new Campground();
		newCampground.setNameString("Tall Boy");
		String actualName = newCampground.getNameString();
		final String expectedName = "Tall Boy";
		Assert.assertEquals(expectedName,actualName);

	}

	@Test
	public void campground_set_and_get_close() {
		Campground newCampground = new Campground();
		newCampground.setCloseDate("10");
		String actualClose = newCampground.getCloseDate();
		final String expectdClose = "10";
		Assert.assertEquals(expectdClose,actualClose);
	}

	@Test
	public void campground_set_and_get_open() {
		Campground newCampground = new Campground();
		newCampground.setOpenDate("04");
		String actualOpen = newCampground.getOpenDate();
		final String expectedOpen = "04";
		Assert.assertEquals(expectedOpen,actualOpen);
	}

	@Test
	public void campground_set_and_get_daily_fee() {
		Campground newCampground = new Campground();
		newCampground.setDailyfee(BigDecimal.valueOf(40.00));
		BigDecimal actualDailyCost = newCampground.getDailyfee();
		final BigDecimal expctedDailyCost = BigDecimal.valueOf(40.00);
		Assert.assertEquals(expctedDailyCost,actualDailyCost);
	}

	@Test
	public void campground_set_and_get_campground_id() {
		Campground newCampground = new Campground();
		newCampground.setCampgroundId(Long.valueOf(849));
		long actualId =newCampground.getCampgroundId();
		final long expectedId = Long.valueOf(849);
		Assert.assertEquals(expectedId,actualId);
	}
	 
	
}
