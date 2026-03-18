package FoodDelivery.auth.kafka;

import org.springframework.kafka.annotation.KafkaListener;

import FoodDelivery.auth.delivery.DeliveryService;

public class kafkaDelivery {
	DeliveryService deliveryService;
	@KafkaListener(topics = "order-prepared", groupId = "delivery-group")
	public void consumePrepared(String orderId){
	
	    deliveryService.startDelivery(orderId);
	
	}
}

