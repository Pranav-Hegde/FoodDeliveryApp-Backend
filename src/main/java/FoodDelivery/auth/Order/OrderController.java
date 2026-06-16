package FoodDelivery.auth.Order;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
public class OrderController {

	// Must match JwtService secret exactly
	private static final String JWT_SECRET = "mysecretkeymysecretkeymysecretkey12";
	private static final Key JWT_KEY = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

	@Autowired
	private OrderService orderService;

	private Claims extractClaimsFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return null;
		}
		try {
			String token = authHeader.substring(7);
			return Jwts.parserBuilder()
					.setSigningKey(JWT_KEY)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			System.err.println("[OrderController] JWT parse failed: " + e.getMessage());
			return null;
		}
	}

	private String extractEmailFromRequest(HttpServletRequest request) {
		Claims claims = extractClaimsFromRequest(request);
		return claims != null ? claims.getSubject() : null;
	}

	@GetMapping
	public ResponseEntity<?> getAllOrders(HttpServletRequest request) {
		Claims claims = extractClaimsFromRequest(request);
		if (claims == null || !"ROLE_SUPER_ADMIN".equals(claims.get("role"))) {
			return ResponseEntity.status(401).body("Unauthorized: Super Admin access required");
		}
		return ResponseEntity.ok(orderService.getAllOrders());
	}

	/** Top-3 orders (any status) – used by Home page shortcut */
	@GetMapping("/my-orders")
	public ResponseEntity<?> getMyOrders(HttpServletRequest request) {
		String email = extractEmailFromRequest(request);
		if (email == null) {
			return ResponseEntity.status(401).body("Unauthorized: valid token required");
		}
		System.out.println("[OrderController] Fetching top 3 orders for: " + email);
		return ResponseEntity.ok(orderService.getOrdersByUserEmail(email));
	}

	/**
	 * Recent Orders page: returns ALL delivered orders for the current user,
	 * stored in DB and filtered by user email + status=DELIVERED.
	 */
	@GetMapping("/my-recent-orders")
	public ResponseEntity<?> getMyRecentOrders(HttpServletRequest request) {
		String email = extractEmailFromRequest(request);
		if (email == null) {
			return ResponseEntity.status(401).body("Unauthorized: valid token required");
		}
		System.out.println("[OrderController] Fetching delivered orders for: " + email);
		return ResponseEntity.ok(orderService.getDeliveredOrdersByUser(email));
	}

	@PostMapping
	public ResponseEntity<Object> createOrder(@RequestBody Order order) {
		return ResponseEntity.ok(orderService.createOrder(order));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrder(@PathVariable String id) {
		return ResponseEntity.ok(orderService.getOrderById(id));
	}

	/**
	 * SSE endpoint: user page connects here to receive real-time status pushes.
	 * The stream is kept open until order is DELIVERED or the client disconnects.
	 */
	@GetMapping(value = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter streamOrderStatus(@PathVariable String id) {
		System.out.println("[SSE] Client subscribed to order: " + id);
		return orderService.subscribeTo(id);
	}

	// ⏱️ Step 1: Kitchen Acceptance with Prep Time commitment
	@PostMapping("/{id}/accept")
	public ResponseEntity<Order> acceptOrder(@PathVariable String id, @RequestBody Map<String, String> payload) {
		long prepTime = Long.parseLong(payload.getOrDefault("prepTime", "30"));
		return ResponseEntity.ok(orderService.acceptOrder(id, prepTime));
	}

	// 🔥 Step 2: Kitchen Completion & Delivery Trigger
	@PostMapping("/{id}/prepare")
	public ResponseEntity<Order> prepareOrder(@PathVariable String id) {
		return ResponseEntity.ok(orderService.prepareOrder(id));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Order> updateOrderStatus(@PathVariable String id, @RequestBody Map<String, Object> updates) {
		return ResponseEntity.ok(orderService.updateOrderDetails(id, updates));
	}
}
