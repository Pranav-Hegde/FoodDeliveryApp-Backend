package FoodDelivery.auth.delivery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DeliveryAgentController {

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @PostMapping
    public DeliveryAgent createDriver(@RequestBody DeliveryAgent agent) {
        return deliveryAgentRepository.save(agent);
    }

    @GetMapping
    public List<DeliveryAgent> getDrivers() {
        return deliveryAgentRepository.findAll();
    }
}