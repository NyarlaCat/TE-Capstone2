package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import com.techelevator.campgrounds.model.Campground;
import com.techelevator.campgrounds.model.CampgroundDAO;
import com.techelevator.campgrounds.model.Park;
import com.techelevator.campgrounds.model.ParkDAO;
import com.techelevator.campgrounds.model.Reservation;
import com.techelevator.campgrounds.model.ReservationDAO;
import com.techelevator.campgrounds.model.Site;
import com.techelevator.campgrounds.model.SiteDAO;
import com.techelevator.campgrounds.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campgrounds.model.jdbc.JDBCParkDAO;
import com.techelevator.campgrounds.model.jdbc.JDBCReservationDAO;
import com.techelevator.campgrounds.model.jdbc.JDBCSiteDAO;
import com.techelevator.campgrounds.view.Menu;

public class CampgroundCLI {
	// Objects we made
	private Menu menu;
	private ReservationDAO reservationDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ParkDAO parkDAO;

	// add menu arrays
	// First menu
	private static final String MAIN_MENU_OPTIONS_VIEW = "View Parks and Campgrounds";
	private static final String MAIN_MENU_OPTIONS_RESERVATION = "Check or Book Reservations";
	private static final String MAIN_MENU_OPTIONS_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTIONS_VIEW,
			MAIN_MENU_OPTIONS_RESERVATION, MAIN_MENU_OPTIONS_EXIT };

	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	// park menu two options go here after selecting main menu 'see all parks'
	private static final String VIEW_MENU_OPTIONS_ALLPARKS = "View detailed Park Info";
	private static final String VIEW_MENU_OPTIONS_ALL_CAMPGROUNDS = "View all Campgrounds by Park";
	private static final String[] VIEW_MENU_OPTIONS = new String[] { VIEW_MENU_OPTIONS_ALLPARKS,
			VIEW_MENU_OPTIONS_ALL_CAMPGROUNDS, MENU_OPTION_RETURN_TO_MAIN };

	// reservation menu
	private static final String RESERVATION_MENU_VIEW_RESERVTIONS = "View reservation";
	private static final String RESERVATION_MENU_BOOK_RESERVATION = "Book Reservation";
	private static final String[] RESERVATION_MENU_OPTIONS = new String[] { RESERVATION_MENU_VIEW_RESERVTIONS,
			RESERVATION_MENU_BOOK_RESERVATION, MENU_OPTION_RETURN_TO_MAIN };

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	// constructor for the CLI
	public CampgroundCLI(DataSource datasource) {
		// create your DAOs here
		this.menu = new Menu(System.in, System.out); // set up menu object
		reservationDAO = new JDBCReservationDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		parkDAO = new JDBCParkDAO(datasource);

	}
 
	public void run() {
		while (true) {
			System.out.println();
			printHeading("Main Menu");
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTIONS_VIEW)) {
				handleViewOptions();
			} else if (choice.equals(MAIN_MENU_OPTIONS_RESERVATION)) {
				handleReservationOptions();
			} else if (choice.equals(MAIN_MENU_OPTIONS_EXIT)) {
				System.out.println("Have a nice Day!");
				System.exit(0);
			}
		}
	}

	private void handleReservationOptions() {
		printHeading("Reservation Options");
		String choice = (String) menu.getChoiceFromOptions(RESERVATION_MENU_OPTIONS);
		if (choice.contentEquals(RESERVATION_MENU_VIEW_RESERVTIONS)) {
			handleDisplayCurrentReservation();
		} else if (choice.contentEquals(RESERVATION_MENU_BOOK_RESERVATION)) {
			handleMakingAReservation();
		}
	}

	private void handleViewOptions() {
		printHeading("View Options");
		String choice = (String) menu.getChoiceFromOptions(VIEW_MENU_OPTIONS);
		if (choice.equals(VIEW_MENU_OPTIONS_ALLPARKS)) {
			handleDisplayingAllParkList();
			handleDisplayingAllParkInfo();
		} else if (choice.equals(VIEW_MENU_OPTIONS_ALL_CAMPGROUNDS)) {
				handleDisplayingCampgroundInfoByPark();
		} else if (choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
			handleReturnToMain();
		}
	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	// write helper methods here for the menu. Here is a place to implement our
	// beautiful class methods
	private void handleDisplayingAllParkList() {
		printHeading("Here's a list of parks");
		List<Park> parkList = parkDAO.getAllParks();
		printListOfParks(parkList);
		System.out.println();
		System.out.println("Select a park by the ID to see more details");



	}

	private void handleDisplayingAllParkInfo() {
		// get the user input of the park id choice

		try {
			long id = menu.getIdInputFromUser(); // not yet idiot proofed for exception handling
			Park thisPark = parkDAO.getParkById(id);
			System.out.println(thisPark.getName() + "\nLocation: " + thisPark.getLocation() + "\nEstablished: "
					+ thisPark.getEstablishedDate() + "\nArea: " + thisPark.getArea() + "\nAnnual Visitors: "
					+ thisPark.getVisitors());
			System.out.print("Description: ");
			int charCount = 0; // formats the text to make it look good
			for (String word : thisPark.getDescription().split(" ")) {

				System.out.print(word + " ");
				charCount += word.length();
				if (charCount >= 60) {
					System.out.println();
					charCount = 0;
				}
			}
		} catch (NullPointerException i) {
			System.out.println("Invalid option selected");
		} finally {
			run();
		}
		;

	}

	private void handleDisplayCurrentReservation() {
		printHeading("Lets find your reservation");
		try {System.out.println("Please enter your confimation #: ");
			Long id = menu.getIdInputFromUser();
		Reservation reservations = (Reservation) reservationDAO.getReservationById(id);
		System.out.println(" Confirmation #: " + reservations.getReservationId() + "\n Site id: "
				+ reservations.getSiteId() + "\n Customer name:" + reservations.getName() + "\n Booked Date: "
				+ reservations.getCreateDate() + "\n Arrival Date: " + reservations.getFromDate()
				+ "\n Departure Date: " + reservations.getToDate());
			
		} catch (Exception e) {
			System.out.println("********** Invalid Entry ******** ");
			run();
		}

	}

	private void handleDisplayingCampgroundInfoByPark() {
		printHeading("Here's a list of parks");
		List<Park> parkList = parkDAO.getAllParks();
		printListOfParks(parkList);
		System.out.println();
		System.out.println("Select a park by the ID to see it's campgrounds");
		try {
			long id = menu.getIdInputFromUser(); // not yet idiot proofed for exception handling
			List<Campground> campgroundList = campgroundDAO.getAllCampgroundsByParkId(id);
			printListOjfCampgrounds(campgroundList);
		} catch (Exception e) {
			System.out.println("********* Invalid Choice *********");
		}

	} 

	private void handleReturnToMain() {
		run();

	}

	private void handleMakingAReservation() {
		printHeading("PARKS");
		List<Park> parkList = parkDAO.getAllParks();
		printListOfParks(parkList);
		System.out.println();
		System.out.println("Select a park by the ID to see it's campgrounds: ");
		try {
			long id = menu.getIdInputFromUser(); // not yet idiot proofed for exception handling

			printHeading("Campgrounds");
			List<Campground> campgroundList = campgroundDAO.getAllCampgroundsByParkId(id);
			printListOjfCampgrounds(campgroundList);
			System.out.println("Please select a campground: ");
			long id2 = menu.getIdInputFromUser();
			System.out.println("Please enter a arrival date: ");
			String arrivalDate = getUserInput("Please enter an Arrival Date as yyyy-mm-dd: ");
			System.out.println("Please enter a departure date");
			String departureDate = getUserInput("Please enter a departure Date as yyyy-mm-dd: ");
			
			//this is some input checking for the dates being good
			LocalDate start = null;
			LocalDate end = null;
			LocalDate now = null;
			try {
				start = LocalDate.parse(arrivalDate);
				end = LocalDate.parse(departureDate);
				now = LocalDate.now();
			} catch (DateTimeParseException  e) {
				
			}
			
			if ((!start.isBefore(end)) && (!start.isAfter(now) && !end.isAfter(now))&& (start.getYear() != end.getYear())) {
				System.out.println("Please try again. Check the dates.\nThe start must be before the end, the reservation must be for a future date and all reservations must be within the same calendar year.");
				run();
			} 
			
			List<Site> site = siteDAO.getAllAvailibleCampsites(id2, arrivalDate, departureDate);
			BigDecimal totalCostOfReservation = campgroundDAO.estimateReservationCost(id2, arrivalDate, departureDate);
			if (site.size() > 0) {
				printHeading("Here are your top 5 results!");
				System.out.println(String.format("%-5s%15s%15s%15s%14s%14s", "Site No", "Max Occup", "Accesible?",
						"Max RV Length", "Utility", "Cost"));
				for (Site s : site) {

					System.out.println("  " + s.getSiteNumber() + "		  " + s.getMaxOccupancy() + " 	        "
							+ (s.getIsAccessible() ? "YES" : "NO") + "	      " + s.getMaxRvLength() + "   	   "
							+ (s.getUtilities() ? "YES" : "N/A") + "		   $"+totalCostOfReservation);
				}
				System.out.println("Please pick a site to reserve");
				long id3 = menu.getIdInputFromUser();
				LocalDate dateStartLocal3 = null;
				LocalDate dateEndLocal3 = null;

				try {
					dateStartLocal3 = LocalDate.parse(arrivalDate);
				} catch (DateTimeParseException e) {

				}
				try {
					dateEndLocal3 = LocalDate.parse(departureDate);
				} catch (DateTimeParseException e) {

				}

				String reservationName = getUserInput("Please enter a name for reservation: ");
				Reservation newReservation = new Reservation();
				newReservation.setSiteId(id3);
				newReservation.setName(reservationName);
				newReservation.setFromDate(dateStartLocal3);
				newReservation.setToDate(dateEndLocal3);
				//newReservation.setCreateDate(null);
				newReservation = reservationDAO.createNewReservation(newReservation);

				long confirmationId = newReservation.getReservationId();
				
				BigDecimal totalcostBigDecimal = reservationDAO.calculateReservationCost(confirmationId);
				System.out.println(
						"Reservation for the " + reservationName + " family has been made, your confirmation # is: "
								+ confirmationId + "\nTotal cost for stay is: $" + totalcostBigDecimal);
			} else {
				System.out.println();
				System.out.println("Sorry no results found");
			}
		} catch (Exception e) {
			System.out.println("********** Invalid option *********");
		}
	}

	// prints a returning list of campgrounds to the consol
	private void printListOjfCampgrounds(List<Campground> campgrounds) {

		if (campgrounds.size() > 0) {
			System.out.println(String.format("%-5s%25s%25s%25s%25s","ID","Name" ,"Opens On" ,"Closes On" ,"Daily Fee"));
			System.out.println();
			for (Campground c : campgrounds) {
				System.out.println(String.format("%-5s%25s%25s%25s%25s",c.getCampgroundId() , c.getNameString() , c.getOpenAsMonth() , 
						 c.getCloseAsMonth() ,"$"+c.getDailyfee()));
		} }else {
			System.out.println("*********    Invalid Choice    **********");
		}
	}

	// prints a returning list of Parks to the consol
	private void printListOfParks(List<Park> parks) {
		System.out.println();
		if (parks.size() > 0) {
			for (Park p : parks) {
				System.out.println(p.getParkIdLong() + ": " + p.getName());
			}
		} else {
			System.out.println("*********    Invalid option    **********");
		}

	}

	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}

}
