package FoodDelivery.auth.Order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FoodDelivery.auth.delivery.DeliveryService;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private DeliveryService deliveryService;

	public Order createOrder(Order order) {
		return orderRepository.save(order);
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Order getOrderById(String id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	public Order acceptOrder(String orderId) {
		Order order = getOrderById(orderId);

		order.setStatus(OrderStatus.ACCEPTED);

		return orderRepository.save(order);
	}

	public Order prepareOrder(String orderId) {
		Order order = getOrderById(orderId);

		order.setStatus(OrderStatus.PREPARED);

		orderRepository.save(order);

		// 🔥 trigger delivery
		deliveryService.startDelivery(orderId);

		return order;
	}

	public Order dispatchOrder(String orderId) {
		Order order = getOrderById(orderId);

		order.setStatus(OrderStatus.DISPATCHED);

		return orderRepository.save(order);
	}
}