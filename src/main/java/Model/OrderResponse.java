package Model;

import java.util.List;

public class OrderResponse {

	private String orderId;
	private List<Product> items;
	private String orderStatus;

	public OrderResponse(String orderId, List<Product> items, String orderStatus) {
		this.orderId = orderId;
		this.items = items;
		this.orderStatus = orderStatus;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public List<Product> getItems() {
		return this.items;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}
}
