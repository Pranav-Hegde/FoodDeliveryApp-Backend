package FoodDelivery.auth.Restaurant;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:5173") 
public class RestaurantController {

    @Autowired
    private AdminService adminService;

    // ✅ Create Restaurant
    @PostMapping
    public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
        return adminService.createRestaurant(restaurant);
    }

    // ✅ Get All Restaurants
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return adminService.getAllRestaurant();
    }

    // ✅ Delete Restaurant
    @DeleteMapping("/{id}")
    public String deleteRestaurant(@PathVariable Long id) {
        return adminService.deleteRestaurant(id);
    }
}