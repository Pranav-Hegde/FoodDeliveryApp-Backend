package FoodDelivery.auth.admin;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import FoodDelivery.auth.Order.Order;
import FoodDelivery.auth.Order.OrderController;
import FoodDelivery.auth.Order.OrderService;
import FoodDelivery.auth.Restaurant.AdminService;
import FoodDelivery.auth.Restaurant.Restaurant;
import FoodDelivery.auth.config.OrderProducer;

@RestController
@RequestMapping("/admin/orders")
public class AdminController {
	private static final Logger logger = LogManager.getLogger(AdminController.class);
	private final OrderService orderservice;

	@Autowired
	private OrderProducer orderProducer;

	public AdminController(OrderService orderservice) {
		this.orderservice = orderservice;
	}

	@GetMapping
	public List<Order> getAllOrders() {
		logger.info("Fetching all orders");
		return orderservice.getAllOrders();
	}

	@PatchMapping("/{orderId}/accept")
	public Order acceptOrder(@PathVariable String orderId, @RequestParam int totalTimeMinutes) {

		Order order = orderservice.acceptOrder(orderId);

		orderProducer.sendOrderAccepted(orderId, totalTimeMinutes);

		return order;
	}

	@PatchMapping("/{orderId}/prepare")
	public Order prepareOrder(@PathVariable String orderId) {
		logger.info("Order {} is being prepared", orderId);
		return orderservice.prepareOrder(orderId);
	}

	@PatchMapping("/{orderId}/dispatch")
	public Order dispatchOrder(@PathVariable String orderId) {
		logger.info("Order {} dispatched", orderId);
		return orderservice.dispatchOrder(orderId);
	}

}
