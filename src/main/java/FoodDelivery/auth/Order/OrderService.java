package FoodDelivery.auth.Order;

import FoodDelivery.auth.delivery.DeliveryAgentRepository;
import FoodDelivery.auth.delivery.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class OrderService {
	long PrepTime;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private DeliveryAgentRepository agentRepository;

	@Autowired
	@Lazy
	private DeliveryService deliveryService;

	// SSE emitter registry: orderId → list of connected user clients
	private final Map<String, CopyOnWriteArrayList<SseEmitter>> emitters = new ConcurrentHashMap<>();

	/**
	 * Register a new SSE emitter for a given order (called when user opens OrderStatus page).
	 */
	public SseEmitter subscribeTo(String orderId) {
		// Timeout 30 min so the page stays connected during a typical delivery
		SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
		emitters.computeIfAbsent(orderId, k -> new CopyOnWriteArrayList<>()).add(emitter);

		Runnable cleanup = () -> {
			CopyOnWriteArrayList<SseEmitter> list = emitters.get(orderId);
			if (list != null) list.remove(emitter);
		};
		emitter.onCompletion(cleanup);
		emitter.onTimeout(cleanup);
		emitter.onError(e -> cleanup.run());

		// Send current state immediately so the client is always up-to-date on connect
		try {
			Order current = orderRepository.findById(orderId).orElse(null);
			if (current != null) {
				emitter.send(SseEmitter.event().name("status").data(current.getStatus()));
			}
		} catch (IOException ignored) {}

		return emitter;
	}

	/**
	 * Push a status update to every client watching this order.
	 */
	private void broadcast(String orderId, String status) {
		CopyOnWriteArrayList<SseEmitter> list = emitters.get(orderId);
		if (list == null || list.isEmpty()) return;

		for (SseEmitter emitter : list) {
			try {
				emitter.send(SseEmitter.event().name("status").data(status));
				// Close the SSE stream once delivered – no need to keep it open
				if ("DELIVERED".equals(status)) {
					emitter.complete();
				}
			} catch (IOException e) {
				emitter.completeWithError(e);
			}
		}
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	/** Top-3 orders (any status) – used by Home page shortcut */
	public List<Order> getOrdersByUserEmail(String email) {
		return orderRepository.findTop3ByUserEmailOrderByCreatedAtDesc(email);
	}

	/** Only DELIVERED orders for the Recent Orders page */
	public List<Order> getDeliveredOrdersByUser(String email) {
		return orderRepository.findByUserEmailAndStatusOrderByCreatedAtDesc(email, "DELIVERED");
	}

	public Order getOrderById(String id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
	}

	public Order acceptOrder(String id, long prepTime) {
		Order order = getOrderById(id);
		order.setStatus("ACCEPTED");
		order.setPrepTime(prepTime);
		order.setAcceptedTime(System.currentTimeMillis());
		Order saved = orderRepository.save(order);
		broadcast(id, saved.getStatus());
		return saved;
	}

	public Order prepareOrder(String id) {
		Order order = getOrderById(id);
		order.setStatus("PREPARED");
		order.setPreparedTime(System.currentTimeMillis());
		Order saved = orderRepository.save(order);

		deliveryService.startDelivery(saved.getId());
		broadcast(id, saved.getStatus());
		return saved;
	}

	public Order createOrder(Order order) {
		order.setStatus("CREATED");
		System.err.println("--- PERSISTING NEW ORDER: " + order.getId() + " ---");
		Order saved = orderRepository.save(order);
		System.err.println("--- SAVE COMPLETE ---");
		return saved;
	}

	public long getPrepTime() {
		return PrepTime;
	}

	public Order updateOrderDetails(String id, Map<String, Object> updates) {
		Order order = getOrderById(id);

		if (updates.containsKey("status")) {
			String newStatus = (String) updates.get("status");
			order.setStatus(newStatus);

			// Auto-set timestamps based on status transitions
			if ("PICKED_UP".equals(newStatus)) {
				order.setPickedUpTime(System.currentTimeMillis());
			} else if ("DELIVERED".equals(newStatus)) {
				order.setDeliveredTime(System.currentTimeMillis());
			}
		}
		if (updates.containsKey("riderId")) {
			Object riderId = updates.get("riderId");
			if (riderId != null) {
				order.setAssignedAgentId(Long.parseLong(riderId.toString()));
			}
		}
		if (updates.containsKey("riderName")) {
			order.setRiderName((String) updates.get("riderName"));
		}
		if (updates.containsKey("riderPhone")) {
			order.setRiderPhone((String) updates.get("riderPhone"));
		}
		if (updates.containsKey("riderPlate")) {
			order.setRiderPlate((String) updates.get("riderPlate"));
		}
		if (updates.containsKey("riderImage")) {
			order.setRiderImage((String) updates.get("riderImage"));
		}
		if (updates.containsKey("estimatedDeliveryMins")) {
			order.setEstimatedDeliveryMins(Integer.parseInt(updates.get("estimatedDeliveryMins").toString()));
		}

		Order saved = orderRepository.save(order);

		// 🔥 Broadcast status change via SSE instantly to all watching clients
		if (updates.containsKey("status")) {
			broadcast(id, saved.getStatus());
		}

		return saved;
	}
}
