package FoodDelivery.auth.Order;


import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderService {

    private final Map<String, Order> orderStore = new ConcurrentHashMap<>();

    public Order placeOrder(String email, List<String> items, double amount) {
        Order order = new Order(email, items, amount);
        orderStore.put(order.getOrderId(), order);
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
}
