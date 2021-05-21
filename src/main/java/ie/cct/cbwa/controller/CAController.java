package ie.cct.cbwa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ie.cct.cbwa.casplittr.exceptions.CloseTripException;
import ie.cct.cbwa.casplittr.exceptions.NullObjectException;
import ie.cct.cbwa.casplittr.exceptions.UnauthorizedException;
import ie.cct.cbwa.casplittr.model.Item;
import ie.cct.cbwa.casplittr.model.Users;
import ie.cct.cbwa.casplittr.util.JWTissuer;
import io.jsonwebtoken.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class CAController {
		
	private ArrayList<Users> allUsers = new ArrayList<Users>();
	
	public Map <String, ArrayList<Item>> expenses = new HashMap<>();
	
	public Map <String, Boolean> isTripOpen = new HashMap<>();
	
	private Users userLoggedIn;
	
	protected CAController() {
		
		populateUsers();
		
	}
	

	/*
	 * THIS METHOD CREATES AN ARRAY WITH ALL USERS AND ADD ALL USERS NEEDED
	 * THIS APPLICATION HAS NOT CONECTION TO A DATABASE, SO ALL USERS HAS TO BE ADDED MANUALY
	 * I'm creating 3 users as requested for this Continuous Assessment
	 */
	private void populateUsers(){
		
		Users user1, user2, user3;
		
		user1 = new Users("Amilcar", "myS3cret");
		allUsers.add(user1);
		
		user2 = new Users("Greg", "myS3cret");
		allUsers.add(user2);
		
		user3 = new Users("David", "myS3cret");
		allUsers.add(user3);
		
	}
	
	
	/* 
	 * THIS METHOD SEARCH FOR A SPECIFIC USER REGISTERED IN THE ARRAYLIST "allUsers"
	 * @return an object (Users)
	 */
	private Users searchUser(String username) {
		
		Users user = new Users("","");
		
		for (Users u: allUsers) {
			
			if (u.getUsername().contentEquals(username)){
				user = u;
			} 
		}	
		return user;
	}
	
	
	/*
	 * THIS METHOD GENERATE ALL THE RESULTS FOR THE SUMMARY
	 * @returns a String with all data to be displayed
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/{trip}/summary")
	public String getSummary(@PathVariable("trip") String trip,
			@RequestHeader(name="Authorization", required=true) String token) {
		
		Claims claims = JWTissuer.decodeJWT(token.split(" ")[1]);
		
		String subClaim = claims.get("sub", String.class);
		
		if (!userLoggedIn.getUsername().contentEquals(subClaim)) {
			
			throw new RuntimeException("Unknown user");
			
		}
		
		CASummary summary = new CASummary(this.expenses,this.userLoggedIn, this.allUsers);
		
		return summary.getSummary();
		
	}
	
	
	/*
	 * THIS METHOD SEARCH FOR AN VALUE OR A EXPENSE IN STORED IN THE ARRAYLIST
	 * @returns an List of all Items/expense which contains part of the String sent as request
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/search")
	public List<Item> getSearch(@RequestParam(name="search", required=true) String search,
			@RequestHeader(name="Authorization", required=true) String token) {
		
		Claims claims = JWTissuer.decodeJWT(token.split(" ")[1]);
		
		String subClaim = claims.get("sub", String.class);
		
		if (!userLoggedIn.getUsername().contentEquals(subClaim)) {
			
			throw new RuntimeException("Unknown user");
			
		}
		
		CASearch mySearch = new CASearch(this.expenses, search);
		
		return mySearch.getSearchResults();
		
		

	}	
	
	
	/*
	 * THIS METHOD RETURN A TOKEN IN CASE THE USER & PASSWORD REQUESTED IS STORED IN THE ARRAYLIST
	 * @return a token (that will be used for all future requests) or a HTTP status "Unauthorized"
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/login") // username and password
	public String login(@RequestParam(name = "username", required = true) String username,
			@RequestParam(name = "password", required = true) String password) {
		
		//compare if the credentials are the same registered in the system
		if(searchUser(username).getUsername().contentEquals(username) && searchUser(username).getPassword().contentEquals(password)) {
			
			this.userLoggedIn = searchUser(username);//tells to the server what user is logged in for future transactions
			
			return JWTissuer.createJWT(username, "ca-splittr", username, 86400000);//Creates the JWT Token for this user
		}
			
		throw new UnauthorizedException("Exception");
		
	}
	
	
	/*
	 * THIS METHOD ADD AN EXPENSE FOR A SPECIFIC TRIP
	 * IN CASE THE TRIP IS CLOSED, AN EXCEPTION WILL BE THROWN
	 * @returns a map with the expense added to it
	 */
	@CrossOrigin(origins = "*")
	@PostMapping("/{trip}/expense")//Authorization: Bearer <token>
	public Map<String, ArrayList<Item>> addExpense(@PathVariable("trip") String trip,
			@RequestHeader(name="Authorization", required=true) String token,
			@RequestBody(required=true) Item item) {
		
		//check is trip is open
		if (isTripOpen.get(trip) != null) {
			
			if (isTripOpen.get(trip).booleanValue() == false) {
				
				throw new CloseTripException("Exception");
			}
		} else {

			isTripOpen.put(trip, true);
		}
		
	
		Claims claims = JWTissuer.decodeJWT(token.split(" ")[1]);
		
		String subClaim = claims.get("sub", String.class);
		
		if (!userLoggedIn.getUsername().contentEquals(subClaim)) {
			
			throw new RuntimeException("Unknown user");
			
		}
		
		item.setUsername(subClaim);//add the user's name to the expense
		
		if(expenses.get(trip) == null) {//creates a new expense list in case it hasn't been created
			
			expenses.put(trip, new ArrayList<Item>());
			
		}
		
		expenses.get(trip).add(item);

		return expenses;

	}
	
	
	/*
	 * THIS METHOD CLOSES A SPECIFIC TRIP
	 * ONCE IS CLOSED, NO MORE EXPENSES CAN BE ADDED
	 * @returns a String with a message/answer of the request
	 */
	@CrossOrigin(origins = "*")
	@PostMapping("/{trip}/close")
	public String closeTrip(@PathVariable("trip") String trip,
			@RequestHeader(name="Authorization", required=true) String token) {
		
		Claims claims = JWTissuer.decodeJWT(token.split(" ")[1]);
		
		String subClaim = claims.get("sub", String.class);
		
		if (!userLoggedIn.getUsername().contentEquals(subClaim)) {
			
			throw new RuntimeException("Unknown user");
			
		}
		

		if (isTripOpen.get(trip) == true) {
			
			isTripOpen.replace(trip, false);
			
			return "You've closed this trip successfully!";
	
		} else if (isTripOpen.get(trip) == false) {
			
			return "This trip is already close";
		}
		
		throw new CloseTripException("Exception");
	}
	

	/*
	 * THIS METHOD SHOW THE EXPENSES IN A ESPECIFIC TRIP REQUESTED
	 * @returns a List with all the expenses in the request
	 */
	@CrossOrigin(origins = "*")
	@GetMapping("/{trip}")
	public List<Item> currentTrip(@PathVariable("trip") String trip,
			@RequestHeader(name="Authorization", required=true) String token) {
		
		Claims claims = JWTissuer.decodeJWT(token.split(" ")[1]);
		
		String subClaim = claims.get("sub", String.class);
		
		if (!userLoggedIn.getUsername().contentEquals(subClaim)) {
			
			throw new RuntimeException("Unknown user");
			
		}
		
		if (expenses.get(trip)==null) {
			
			throw new NullObjectException("Exception");
		}

		return expenses.get(trip);

	}
	
	
	
	
	//===================================================================================
	
	
}
