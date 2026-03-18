package FoodDelivery.auth.notificationservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @KafkaListener(
            topics="order-topic",
            groupId="notification-group")
    public void consume(String message){

        System.out.println("Notification received: " + message);

    }

}