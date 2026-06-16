package FoodDelivery.auth.delivery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import FoodDelivery.auth.Order.Order;
import FoodDelivery.auth.Order.OrderService;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

@Service
public class DeliveryService {
    private static final Logger logger = LogManager.getLogger(DeliveryService.class);

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepo;

    @Autowired
    private OrderService orderService; // Note: Use @Lazy if you get circular dependency

    @Autowired
    private ExecutorService executor;

    public void startDelivery(String orderId) {
        executor.submit(() -> {
            try {
                Order order = orderService.getOrderById(orderId);
                logger.info("Order #" + orderId + " prepared and ready for driver pickup. Waiting for a driver to claim...");
            } catch (Exception e) {
                logger.error("Error in delivery executor: " + e.getMessage());
            }
        });
    }
}
