package Model;

public class Customer {
	
	private String customerId;
	private String firstName;
	private String lastName;
	private String email;
	private String salt;
	private String password;
	private String address;
	
	public Customer() {}
	
	public Customer(String customerId, String firstName, String lastName, String email, String salt, String password, String address) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.salt = salt;
		this.password = password;
		this.address = address;
	}
	
	public String getCustomerId() {
		return this.customerId;
	}
	
	public String getSalt() {
		return this.salt;
	}
	
	public String getPassword() {
		return this.password;
	}
	
}