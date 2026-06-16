package FoodDelivery.auth.cart;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173") // Adjust port to your React app
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{email}")
    public List<CartItem> getCart(@PathVariable String email) {
        return cartService.getCartByUser(email);
    }

    @PostMapping("/add")
    public CartItem addToCart(@RequestBody CartItem item) {
        return cartService.addItemToCart(item);
    }

    @DeleteMapping("/remove/{id}")
    public void removeFromCart(@PathVariable Long id) {
        cartService.removeItem(id);
    }

    @DeleteMapping("/clear/{email}")
    public void clearCart(@PathVariable String email) {
        cartService.clearCart(email);
    }
}
