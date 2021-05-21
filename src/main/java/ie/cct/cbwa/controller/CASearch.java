package ie.cct.cbwa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ie.cct.cbwa.casplittr.model.Item;

public class CASearch {
		
	private Map <String, ArrayList<Item>> myMap = new HashMap<>();
	
	private String search;
	
	
	public CASearch(Map <String, ArrayList<Item>> myMap, String search) {
		
		this.myMap = myMap;
		
		this.search = search;
		
	}

	
	/*
	 * THIS METHOD MAKES A LOOP IN THE WHOLE MAP LOOKING FOR DESCRIPTIONS AND AMOUNT THAT MATCHED WITH DATA STORED
	 * @return an ArrayList with all results matched on this search
	 */
	public List<Item> getSearchResults() {
		
		List<Item> results = new ArrayList<>();
		
		Iterator<Map.Entry<String, ArrayList<Item>>> myIterator = this.myMap.entrySet().iterator();
		
		while(myIterator.hasNext()){
             Map.Entry<String, ArrayList<Item>> entry = myIterator.next();
             
             for(Item i: entry.getValue()){
                 
                 if(i.getDescription().contains(this.search) || i.getAmount().toString().contains(this.search)) {
                	 
                	 results.add(i);
                	 
                 }
                 
             }
        }
	
		return results;
	}

}
