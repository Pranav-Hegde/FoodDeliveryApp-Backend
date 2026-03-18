package FoodDelivery.auth.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final Logger logger = LogManager.getLogger(OrderProducer.class);
    public void sendOrderAccepted(String orderId, int totalTime) {

        String message = orderId + ":" + totalTime;

        kafkaTemplate.send("order-accepted-topic", message);

        logger.info("Order event sent to Kafka: " + message);
    }
}