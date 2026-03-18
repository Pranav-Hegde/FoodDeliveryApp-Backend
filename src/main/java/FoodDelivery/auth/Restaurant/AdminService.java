package FoodDelivery.auth.Restaurant;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FoodDelivery.auth.controller.AuthController;

@Service
public class AdminService {
	private static final Logger logger = LogManager.getLogger(AdminService.class.getName());
	@Autowired
	private RestauarantRepository restaurantRepository;
	
	public Restaurant createRestaurant(Restaurant restaurant) {
		logger.info("Admin creating restaurant: {}", restaurant.getName());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

	    logger.info("Restaurant created successfully with id: {}", savedRestaurant.getId());
	    return savedRestaurant;
	}
	
	public List<Restaurant> getAllRestaurant(){
		logger.info("Fetching all restaurants");

        List<Restaurant> restaurants = restaurantRepository.findAll();

        logger.info("Total restaurants found: {}", restaurants.size());
        return restaurants;
		
	}
	public String deleteRestaurant(Long id) {

        logger.warn("Admin requested deletion of restaurant id: {}", id);

        restaurantRepository.deleteById(id);

        logger.info("Restaurant deleted successfully with id: {}", id);
        return "Restaurant deleted successfully";

	}
}
