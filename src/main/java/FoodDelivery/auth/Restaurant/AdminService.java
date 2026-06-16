package FoodDelivery.auth.Restaurant;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private static final Logger logger = LogManager.getLogger(AdminService.class.getName());

    @Autowired
    private RestauarantRepository restaurantRepository;

    /**
     * Creates a new restaurant listing.
     */
    public Restaurant createRestaurant(Restaurant restaurant) {
        logger.info("Admin creating restaurant: {}", restaurant.getName());
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        logger.info("Restaurant created successfully with id: {}", savedRestaurant.getId());
        return savedRestaurant;
    }

    /**
     * Fetches all restaurants for the dashboard.
     */
    public List<Restaurant> getAllRestaurant() {
        logger.info("Fetching all restaurants");
        List<Restaurant> restaurants = restaurantRepository.findAll();
        logger.info("Total restaurants found: {}", restaurants.size());
        return restaurants;
    }

    /**
     * Fetches a specific restaurant by ID.
     */
    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found at ID: " + id));
    }

    /**
     * Deletes a restaurant and its associated data.
     */
    public String deleteRestaurant(Long id) {
        logger.warn("Admin requested deletion of restaurant id: {}", id);
        restaurantRepository.deleteById(id);
        logger.info("Restaurant deleted successfully with id: {}", id);
        return "Restaurant deleted successfully";
    }

    /**
     * Updates restaurant details and synchronizes the menu catalog.
     */
    public Restaurant updateRestaurant(Long id, Restaurant updatedRes) {
        logger.info("Updating restaurant ID: {}", id);
        
        // Find the existing restaurant using the @Autowired repository
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot update: Restaurant not found"));

        // Update basic details
        existing.setName(updatedRes.getName());
        existing.setCuisine(updatedRes.getCuisine());
        existing.setLocation(updatedRes.getLocation());
        existing.setAddress(updatedRes.getAddress());
        existing.setPhone(updatedRes.getPhone());
        existing.setImageUrl(updatedRes.getImageUrl());
        existing.setRating(updatedRes.getRating());
        existing.setDeliveryTime(updatedRes.getDeliveryTime());

        // Sync the Menu catalog
        // We clear and addAll to maintain the same collection instance (better for JPA tracking)
        if (existing.getMenu() != null && updatedRes.getMenu() != null) {
            existing.getMenu().clear();
            existing.getMenu().addAll(updatedRes.getMenu());
            logger.info("Menu synchronized for restaurant: {}", existing.getName());
        }

        // Save and return the updated entity
        return restaurantRepository.save(existing);
    }
}
