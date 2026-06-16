package FoodDelivery.auth.Order;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "food_orders") // Changed to avoid foreign key drop errors with old tables
public class Order {
	@Id
	private String id; // CHANGED: Now a String ID

	private String userEmail;
	private String userName;
	private String status;
	private double totalAmount;

	private String restaurantName;
	private String restaurantId;
	private String address;
	private String paymentId;

	private long prepTime;
	private long acceptedTime;
	private long preparedTime;

	private Long assignedAgentId;
	private String riderName;
	private String riderPhone;
	private String riderPlate;
	private String riderImage;

	private long pickedUpTime;
	private int estimatedDeliveryMins;
	private long deliveredTime;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "food_orders_items", joinColumns = @JoinColumn(name = "order_id"))
	@Column(name = "item")
	private List<String> items = new ArrayList<>();

	private LocalDateTime createdAt = LocalDateTime.now();
	private int totalMinutes;

	// Constructor to auto-generate unique String IDs if not provided
	public Order() {
		this.id = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	}

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getAssignedAgentId() {
		return assignedAgentId;
	}

	public void setAssignedAgentId(Long assignedAgentId) {
		this.assignedAgentId = assignedAgentId;
	}

	public String getRiderName() {
		return riderName;
	}

	public void setRiderName(String riderName) {
		this.riderName = riderName;
	}

	public String getRiderPhone() {
		return riderPhone;
	}

	public void setRiderPhone(String riderPhone) {
		this.riderPhone = riderPhone;
	}

	public String getRiderPlate() {
		return riderPlate;
	}

	public void setRiderPlate(String riderPlate) {
		this.riderPlate = riderPlate;
	}

	public String getRiderImage() {
		return riderImage;
	}

	public void setRiderImage(String riderImage) {
		this.riderImage = riderImage;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public long getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(long prepTime) {
		this.prepTime = prepTime;
	}

	public long getAcceptedTime() {
		return acceptedTime;
	}

	public void setAcceptedTime(long acceptedTime) {
		this.acceptedTime = acceptedTime;
	}

	public long getPreparedTime() {
		return preparedTime;
	}

	public void setPreparedTime(long preparedTime) {
		this.preparedTime = preparedTime;
	}

	public int getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(int totalMinutes) {
		this.totalMinutes = totalMinutes;
	}

	public long getPickedUpTime() {
		return pickedUpTime;
	}

	public void setPickedUpTime(long pickedUpTime) {
		this.pickedUpTime = pickedUpTime;
	}

	public int getEstimatedDeliveryMins() {
		return estimatedDeliveryMins;
	}

	public void setEstimatedDeliveryMins(int estimatedDeliveryMins) {
		this.estimatedDeliveryMins = estimatedDeliveryMins;
	}

	public long getDeliveredTime() {
		return deliveredTime;
	}

	public void setDeliveredTime(long deliveredTime) {
		this.deliveredTime = deliveredTime;
	}
}
