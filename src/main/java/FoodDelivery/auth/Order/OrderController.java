package FoodDelivery.auth.Order;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping 
	public List<Order> getAllOrders() {
		return orderService.getAllOrders();
	}

	@PostMapping
	public Order createOrder(@RequestBody Order order) {
		return orderService.createOrder(order);
	}

	@PostMapping("/{id}/accept")
	public Order acceptOrder(@PathVariable String id) {
		return orderService.acceptOrder(id);
	}

	@PostMapping("/{id}/prepare")
	public Order prepareOrder(@PathVariable String id) {
		return orderService.prepareOrder(id);
	}
}