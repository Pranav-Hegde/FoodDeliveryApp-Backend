package FoodDelivery.auth.delivery;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAgentRepository
        extends JpaRepository<DeliveryAgent, Long> {

    Optional<DeliveryAgent> findFirstByAvailableTrue();
    Optional<DeliveryAgent> findByEmail(String email);
}