package FoodDelivery.auth.Restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String city;
	private String address;
	private String phone;
	private String imageUrl;
	private Double rating;
	private String deliveryTime;

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public Long getId() {
		return id;
	}

	public String getCity() {
		// TODO Auto-generated method stub
		return city;
	}

	public String getaddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public String getimageUrl() {
		return imageUrl;
	}

	public Double getRating() {
		return rating;
	}

	public String getDeliverytime() {
		return deliveryTime;
	}
}