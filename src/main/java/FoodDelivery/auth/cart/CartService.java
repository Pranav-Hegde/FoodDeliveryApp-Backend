package FoodDelivery.auth.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public List<CartItem> getCartByUser(String email) {
        return cartRepository.findByUserEmail(email);
    }

    public CartItem addItemToCart(CartItem newItem) {
        // Check if item already in cart for this user
        return cartRepository.findByUserEmailAndFoodName(newItem.getUserEmail(), newItem.getFoodName())
            .map(existingItem -> {
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                return cartRepository.save(existingItem);
            })
            .orElseGet(() -> cartRepository.save(newItem));
    }

    public void removeItem(Long id) {
        cartRepository.deleteById(id);
    }

    @Transactional
    public void clearCart(String email) {
        cartRepository.deleteByUserEmail(email);
    }
}
