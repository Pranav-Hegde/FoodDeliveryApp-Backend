package FoodDelivery.auth.security;

import org.springframework.context.annotation.Configuration;

// JWT authentication is handled directly in each controller via extractEmailFromRequest().
// No Servlet filter is needed — previously the filter was intercepting requests
// and returning 401 before controllers could process them.
@Configuration
public class SecurityConfig {
    // No filter beans registered — controllers parse JWT tokens themselves.
}
