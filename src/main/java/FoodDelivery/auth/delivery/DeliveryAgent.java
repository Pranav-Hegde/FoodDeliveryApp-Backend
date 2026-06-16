package FoodDelivery.auth.delivery;

import jakarta.persistence.*;

@Entity
public class DeliveryAgent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String name;
	private String phone;

	private String vehicleType; // Motorcycle, Car, etc.
	private String vehiclePlate; // KA 01 EK 9982

	private String status; // AVAILABLE, DELIVERING, OFFLINE
	private double rating = 5.0;

	private String profileImage; // URL of profile image

	private String currentOrderId; // Track assigned order

	private boolean available; // true = free, false = busy

	// ✅ Constructors
	public DeliveryAgent() {
	}

	public DeliveryAgent(String name, String phone, String vehicleType, String vehiclePlate) {
		this.name = name;
		this.phone = phone;
		this.vehicleType = vehicleType;
		this.vehiclePlate = vehiclePlate;
		this.status = "AVAILABLE";
		this.available = true;
	}

	// Getters & Setters

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehiclePlate() {
		return vehiclePlate;
	}

	public void setVehiclePlate(String vehiclePlate) {
		this.vehiclePlate = vehiclePlate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public String getCurrentOrderId() {
		return currentOrderId;
	}

	public void setCurrentOrderId(String string) {
		this.currentOrderId = string;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}
}