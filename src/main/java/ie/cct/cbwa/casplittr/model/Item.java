package ie.cct.cbwa.casplittr.model;

public class Item {
	
	private String description;
	private Double amount;
	private String username;
	
	public Item() {
		super();
	}
	
	public Item(String description, Double amount, String username) {
		super();
		this.description = description;
		this.amount = amount;
		this.username = username;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
		

}
