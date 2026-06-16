package FoodDelivery.auth.Order;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {

	Optional<Order> findById(String id);

	// Top-3 recent orders for home page shortcut (any status)
	java.util.List<Order> findTop3ByUserEmailOrderByCreatedAtDesc(String userEmail);

	// Recent orders page: only DELIVERED orders, latest first, per user
	java.util.List<Order> findByUserEmailAndStatusOrderByCreatedAtDesc(String userEmail, String status);

}