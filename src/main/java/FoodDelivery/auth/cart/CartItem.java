package FoodDelivery.auth.cart;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "cart_items")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userEmail; // Links the cart to a specific user
	private Long restaurantId; // The restaurant the item belongs to
	private String foodName;
	private Double price;
	private Integer quantity;
	private String image; // URL to the food image

	public String getUserEmail() {
		// TODO Auto-generated method stub
		return userEmail;
	}

	public String getFoodName() {
		// TODO Auto-generated method stub
		return foodName;
	}

	public int getQuantity() {
		// TODO Auto-generated method stub
		return quantity;
	}

	public void setQuantity(int quantity) {
		// TODO Auto-generated method stub
		this.quantity = quantity;
	}

}
