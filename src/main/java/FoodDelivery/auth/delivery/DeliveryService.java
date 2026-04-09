package FoodDelivery.auth.delivery;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import FoodDelivery.auth.Order.Order;
import FoodDelivery.auth.Order.OrderService;

@Service
public class DeliveryService {
	private static final Logger logger = LogManager.getLogger(DeliveryService.class);

	@Autowired
	private DeliveryAgentRepository deliveryAgent;

	private OrderService orderService;

	@Autowired
	private ExecutorService executor;

	public void startDelivery(String orderId) {
		executor.submit(() -> {
			Order order = orderService.getOrderById(orderId);

			long acceptedTime = order.getAcceptedTime();
			long preparedTime = order.getPreparedTime();

			int total = order.getTotalMinutes();

			long usedMinutes = (preparedTime - acceptedTime) / 60000;
			int remainingMinutes = total - (int) usedMinutes;

			if (remainingMinutes < 10) {
				remainingMinutes = 10;
			}
			assignDriver(orderId, remainingMinutes);
		});
	}

	private void assignDriver(String orderId, int remainingMinutes) {
		Optional<DeliveryAgent> driverAvailable = deliveryAgent.findFirstByAvailableTrue();

		if (driverAvailable.isPresent()) {
			DeliveryAgent driver = driverAvailable.get();

			driver.setAvailable(false);
			driver.setCurrentOrderId(orderId);
			driver.setStatus("BUSY");
			deliveryAgent.save(driver);
			logger.info("Captain " + driver.getName() + " assigned to your Order ");
		} else {
			logger.info("Sorry no Captains Available right Now!, Kindly try again after sometime");
		}

	}
}
