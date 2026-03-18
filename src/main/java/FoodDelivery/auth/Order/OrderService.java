package FoodDelivery.auth.Order;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FoodDelivery.auth.admin.AdminController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Service
public class OrderService {
	private static final Logger logger = LogManager.getLogger(OrderService.class);
    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();
    @Autowired
    private ExecutorService executor;
    
    public Order placeOrder(String email, List<String> items, double amount) {
    	Order order = new Order(email, items, amount);
    	
      executor.submit(() ->{
       try {
        orderStore.put(order.getOrderId(), order);
        logger.info("Order Placed for "+email);
        Thread.sleep(5000);
        }catch(Exception e){
        	logger.error(e.getMessage());
        }
    });
	  return order;
    }

    public Order getOrderById(String orderId) {
        return orderStore.get(orderId);
    }

    public List<Order> getOrdersByUser(String email) {

        List<Order> orders = new ArrayList<>();

        for (Order order : orderStore.values()) {
            if (order.getUserEmail().equals(email)) {
                orders.add(order);
            }
        }

        return orders;
    }

    public List<Order> getAllOrders() {
        return new ArrayList<>(orderStore.values());
    }

    public Order updateStatus(String orderId, String status) {

        Order order = orderStore.get(orderId);

        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        OrderStatus orderStatus =
                OrderStatus.valueOf(status.toUpperCase());

        order.setStatus(orderStatus);

        return order;
    }
}