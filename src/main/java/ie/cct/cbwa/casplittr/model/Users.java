package ie.cct.cbwa.casplittr.model;

public class Users {
	private String username;
	private String password;
	private Double personalExpense;
	
	public Users(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Double getPersonalExpense() {
		return personalExpense;
	}

	public void setPersonalExpense(Double personalExpense) {
		this.personalExpense = personalExpense;
	}

}
