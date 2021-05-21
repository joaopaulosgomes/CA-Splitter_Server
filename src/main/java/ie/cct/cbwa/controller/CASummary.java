package ie.cct.cbwa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ie.cct.cbwa.casplittr.model.Item;
import ie.cct.cbwa.casplittr.model.Users;
import ie.cct.cbwa.controller.*;

public class CASummary {
	
	private String havePaid, differencePayment, totalPaid, numberOfPurchases, highestExpense, lowestExpense, avarageExpense;
	
	private Map <String, ArrayList<Item>> map = new HashMap<>();
	
	private ArrayList<Users> allUsers;
	
	public CASummary(Map <String, ArrayList<Item>> myMap, Users user, ArrayList<Users> allUsers) {
		
		this.map = myMap;
		
		this.allUsers = allUsers;
		
		this.havePaid = thisUserhasPaid(user).toString();//what this user have paid in total
		
		this.differencePayment = differecePayment(totalPaid(), user).toString();//What they need to pay/get paid
		
		this.totalPaid = totalPaid().toString();//The totals of the trip
		
		this.numberOfPurchases = numberOfPurchases().toString();//the number of purchases in total
		
		this.highestExpense = highestExpense().getDescription() +":"+ highestExpense().getAmount();//the highest expense on this trip
		
		this.lowestExpense = lowestExpense().getDescription() + ":" + lowestExpense().getAmount().toString();//the lowest expense on this trip
		
		this.avarageExpense = avarageExpense();//the average among all purchases
		
	}
	
	
	/*
	 * THIS METHOD GET THE PIECES TOGETHER AND CREATES AN SINGLE STRING WITH ALL THE VALUES TO BE DISPLAYED IN THE MOBILE APP
	 * #1 PIECE: Have Paid
	 * #2 PIECE: Need to pay/get Paid
	 * #3 PIECE: Totals
	 * #4 PIECE: Number of Purchases
	 * #5 PIECE: Highest Expense
	 * #6 PIECE: Lowest Expense
	 * #7 PIECE: Average Expense
	 * 
	 * @returns a single String with all values
	 */
	public String getSummary() {
		
		return
		
		this.havePaid + ":" + this.differencePayment +  ":" + this.totalPaid +

		":" + this.numberOfPurchases + ":" + this.highestExpense +
		
		":" + this.lowestExpense + ":" + this.avarageExpense;

	}


	/*
	 * THIS METHOD SOLVE THE EQUATION OF
	 * WHAT THE USER LOGGED IN HAS PAID IN TOTAL
	 * @returns the total (Double)
	 */
	private Double thisUserhasPaid(Users user) {
		
		Iterator<Map.Entry<String, ArrayList<Item>>> myIterator = this.map.entrySet().iterator();
		
		Double total = 0.0;
		
		while(myIterator.hasNext())
        {
             Map.Entry<String, ArrayList<Item>> entry = myIterator.next();
             
             for(Item i: entry.getValue()){
                 
                 if(i.getUsername().contains(user.getUsername())) {
                	 total = total + i.getAmount();
                 }
                 
             }
        }
		
		return total;
		
	}
	
	
	/*
	 * THIS METHOD SOLVE THE EQUATION OF
	 * WHAT THEY NEEP TO PAY OR GET PAID 
	 */
	private String differecePayment(Double total, Users user) {
		
		for (Users u: this.allUsers) { 
			   
			u.setPersonalExpense(thisUserhasPaid(u));
               
	    }
		
		Double result = (total/allUsers.size()) - user.getPersonalExpense();
		
		if(result>0) {
			return "Pay " + Math.abs(result);
			
		} else if (result<0) {
			return " Refund " + Math.abs(result);
			
		}

		return "Doesn't owe anything";
		
	}
	
	
	/*
	 * THIS METHOD SOLVE THE EQUATION OF
	 * THE TOTALS OF THE TRIP
	 * @returns the total (Double)
	 */
	private Double totalPaid() {
		
		Iterator<Map.Entry<String, ArrayList<Item>>> myIterator = this.map.entrySet().iterator();
		
		Double total = 0.0;
		
		while(myIterator.hasNext())
        {
             Map.Entry<String, ArrayList<Item>> entry = myIterator.next();
             
             for(Item i: entry.getValue()){
                 
                 total = total+i.getAmount();
                 
             }
        }
		
		return total;
	
	}
	
	
	/*
	 * @returns the number of purchases made for this uses
	 */
	private Integer numberOfPurchases() {
		
		Iterator<Map.Entry<String, ArrayList<Item>>> myIterator = this.map.entrySet().iterator();
		
        int count = 0;
		
		while(myIterator.hasNext())
		  {
		       Map.Entry<String, ArrayList<Item>> entry = myIterator.next();
		       
		       for(Item i: entry.getValue()){
		           
		           count++;
		           
		       }
		  }
	
      return count;
      	
	}
	
	
	/*
	 * THIS METHOD USES A QUICK SORT ALGORITHM FOR FINDING THE HIGHEST EXPENSE STORED
	 * 
	 */
	private Item highestExpense() {
		
		Iterator<Map.Entry<String, ArrayList<Item>>> myIterator = this.map.entrySet().iterator();
	
		//this arraylist is created to store all the values in the Map
	    ArrayList<Double> highest = new ArrayList<>();
	
		
	    while(myIterator.hasNext())
	    {
	         Map.Entry<String, ArrayList<Item>> entry = myIterator.next();
	         
	         for(Item i: entry.getValue()){
	             
	             highest.add(i.getAmount());
	             
	         }
	    }
	    
	    
	    highest = quickSort(highest);
	    
	    Double amountHighest = highest.get(highest.size()-1);
	    
	    return searchExpense(amountHighest);
		
	}
	
	
	/*
	 * THIS METHOD USES A QUICK SORT ALGORITHM FOR FINDING THE LOWEST EXPENSE STORED
	 */
	private Item lowestExpense(){
		
		Iterator<Map.Entry<String, ArrayList<Item>>> myIterator = this.map.entrySet().iterator();
		
		//this arraylist is created to store all the values in the Map
	    ArrayList<Double> lowest = new ArrayList<>();
	
		
	    while(myIterator.hasNext())
	    {
	         Map.Entry<String, ArrayList<Item>> entry = myIterator.next();
	         
	         for(Item i: entry.getValue()){
	             
	             lowest.add(i.getAmount());
	             
	         }
	    }
	    
	    
	    lowest = quickSort(lowest);
	    
	    Double amountLowest = lowest.get(0);
	    
	    return searchExpense(amountLowest);
			
	}
	
	
	/*
	 * @return the average of the expenses registered in the map 
	 */
	private String avarageExpense() {
		
		Double total = totalPaid();
		
		Double nPurchases = numberOfPurchases().doubleValue();
		
		Double result = total/nPurchases;
		
		return result.toString();
		
	}
	
	
	/*
	 * THIS METHOD SORT THE ARRAYLIST OF ITEMS USING THE QUICK SORT ALGORITHM
	 */
	private ArrayList<Double> quickSort(ArrayList<Double> myList) {
		  
      	if (myList.size() <= 1){
      		
      		return myList; // Already sorted 
      	} 
         

        ArrayList<Double> sorted = new ArrayList<>();
        ArrayList<Double> lesser = new ArrayList<Double>();
        ArrayList<Double> greater = new ArrayList<Double>();
        Double pivot = myList.get(myList.size()-1); // Uses last element as pivot
        
        for (int i = 0; i < myList.size()-1; i++)
        {
       
        if (myList.get(i).compareTo(pivot) < 0)
            lesser.add(myList.get(i));    
        else
            greater.add(myList.get(i));   
        }

        lesser = quickSort(lesser);
        greater = quickSort(greater);

        lesser.add(pivot);
        lesser.addAll(greater);
        sorted = lesser;

        return sorted;
	  
	  }
	
	
	/*
	 * @returns a specific expense which matches in the Map of expenses
	 */
	private Item searchExpense(Double search) {
		
		Iterator<Map.Entry<String, ArrayList<Item>>> myIterator = this.map.entrySet().iterator();
		
		while(myIterator.hasNext())
        {
             Map.Entry<String, ArrayList<Item>> entry = myIterator.next();
             
             for(Item i: entry.getValue()){
                 
                 if(i.getAmount() == search) {
                	 return i;
                 }
                 
             }
        }
		
		return null;
	}
	
	

}


