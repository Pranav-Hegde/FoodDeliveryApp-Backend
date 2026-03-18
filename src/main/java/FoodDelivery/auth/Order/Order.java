package FoodDelivery.auth.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {

	private final String orderId;
	private String userEmail;
	private List<String> items;
	private double totalAmount;
	private OrderStatus status;
	private final LocalDateTime createdAt;

	public Order(String userEmail, List<String> items, double totalAmount) {
		this.orderId = UUID.randomUUID().toString();
		this.userEmail = userEmail;
		this.items = items;
		this.totalAmount = totalAmount;
		this.status = OrderStatus.CREATED;
		this.createdAt = LocalDateTime.now();
	}

	public String getOrderId() {
		return orderId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public List<String> getItems() {
		return items;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	private long preparedTime;
	private long acceptedTime;

	public void setStatus(OrderStatus status) {
		this.status = status;
		if(status == OrderStatus.ACCEPTED) {
			this.acceptedTime = System.currentTimeMillis();
		}
		
		if(status==OrderStatus.PREPARED) {
			this.preparedTime = System.currentTimeMillis();
		}
	}

	public int getTotalMinutes() {
		// TODO Auto-generated method stub
		return (int) ((preparedTime - acceptedTime) / (1000 * 60));
	}

	public long getPreparedTime() {
		// TODO Auto-generated method stub
		return preparedTime;
	}

	public long getAcceptedTime() {
		// TODO Auto-generated method stub
		return acceptedTime;
	}
}