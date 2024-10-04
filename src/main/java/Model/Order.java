package Model;

public class Order {

	private String orderId;
	private String customerId;
	private String orderDetail;
	private String orderStatus;

	public Order(String orderId, String customerId, String orderDetail, String orderStatus) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.orderDetail = orderDetail;
		this.orderStatus = orderStatus;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public String getOrderDetail() {
		return this.orderDetail;
	}

	public String getOrderStatus() {
		return this.orderStatus;
	}
}