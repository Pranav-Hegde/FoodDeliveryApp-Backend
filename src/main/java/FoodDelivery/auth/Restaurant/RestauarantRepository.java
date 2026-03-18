package FoodDelivery.auth.Restaurant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RestauarantRepository extends JpaRepository<Restaurant, Long> {
}
