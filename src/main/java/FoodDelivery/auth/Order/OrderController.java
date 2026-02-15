package FoodDelivery.auth.Order;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger =
            LogManager.getLogger(OrderController.class);

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Order placeOrder(
            HttpServletRequest request,
            @RequestBody String body,
            @RequestParam double amount) {

        String email = (String) request.getAttribute("email");
        body = body.replace("[", "")
                .replace("]", "")
                .replace("\"", "");

     List<String> items = List.of(body.split(","));
     logger.info("Placing order for user={}", email);
     return orderService.placeOrder(email, items, amount);

        
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/my")
    public List<Order> myOrders(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        return orderService.getOrdersByUser(email);
    }
}
