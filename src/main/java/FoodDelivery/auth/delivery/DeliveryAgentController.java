package FoodDelivery.auth.delivery;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/delivery-agents")
public class DeliveryAgentController {

    @Autowired
    private DeliveryAgentRepository repository;

    @GetMapping
    public List<DeliveryAgent> getAllAgents() {
        return repository.findAll();
    }

    @GetMapping("/by-email")
    public ResponseEntity<?> getAgentByEmail(@RequestParam String email) {
        Optional<DeliveryAgent> agent = repository.findByEmail(email);
        if (agent.isPresent()) {
            return ResponseEntity.ok(agent.get());
        }
        return ResponseEntity.status(404).body("Agent not found");
    }

    @PostMapping("/onboard")
    public DeliveryAgent onboardAgent(@RequestBody DeliveryAgent agent) {
        agent.setStatus("AVAILABLE");
        agent.setAvailable(true);
        return repository.save(agent);
    }
    
    @PutMapping("/{id}/status")
    public DeliveryAgent updateStatus(@PathVariable Long id, @RequestParam String status) {
        DeliveryAgent agent = repository.findById(id).orElseThrow();
        agent.setStatus(status);
        return repository.save(agent);
    }
}
