package Model;

public class Product {
	
	private String productId;
	private String productName;
	private String productDescription;
	private double price;
	private int quantity;
	private String category;
	
	public Product(String productId, String productName, String productDescription, double price, int quantity, String category) {
		this.productId = productId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
	}
	
	public String getProductId() {
		return this.productId;
	}
	
	public String getProductName() {
		return this.productName;
	}
	
	public String getProductDescription() {
		return this.productDescription;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public int getQuantity() {
		return this.quantity;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public String toString() {
		return "Product[" + productId + ", " + productName + ", " + productDescription + ", " + price + ", " + quantity + ", " + category + "]";
	}
}