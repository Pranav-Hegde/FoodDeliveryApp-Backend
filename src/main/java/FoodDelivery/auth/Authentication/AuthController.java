package FoodDelivery.auth.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/check-existence")
    public ResponseEntity<?> checkExistence(@RequestParam String query) {
        // query could be email or phone
        Optional<User> user = userService.findByEmail(query).isPresent() 
                                ? userService.findByEmail(query) 
                                : userService.findByPhone(query);
        
        return ResponseEntity.ok(new UserStatusResponse(
            user.isPresent(), 
            user.map(User::getRole).orElse(null),
            user.map(User::getName).orElse(null)
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (userService.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already registered");
        }
        
        User user = userService.registerUser(
            req.getEmail(), 
            req.getPhone(), 
            req.getName(), 
            req.getPassword(), 
            req.getRole() != null ? req.getRole() : "ROLE_USER"
        );
        
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole(), user.getName(), user.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if ("admin".equals(req.getIdentifier()) && "pranav@123".equals(req.getPassword())) {
            String token = jwtService.generateToken("admin", "ROLE_SUPER_ADMIN");
            return ResponseEntity.ok(new AuthResponse(token, "ROLE_SUPER_ADMIN", "Super Admin", "admin"));
        }

        Optional<User> userOpt = userService.findByEmail(req.getIdentifier()).isPresent() 
                                ? userService.findByEmail(req.getIdentifier()) 
                                : userService.findByPhone(req.getIdentifier());
        
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(req.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        
        User user = userOpt.get();
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return ResponseEntity.ok(new AuthResponse(token, user.getRole(), user.getName(), user.getEmail()));
    }
}

class UserStatusResponse {
    private boolean exists;
    private String role;
    private String name;
    
    public UserStatusResponse(boolean exists, String role, String name) {
        this.exists = exists;
        this.role = role;
        this.name = name;
    }
    
    public boolean isExists() { return exists; }
    public String getRole() { return role; }
    public String getName() { return name; }
}

class RegisterRequest {
    private String email;
    private String phone;
    private String name;
    private String password;
    private String role;
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

class LoginRequest {
    private String identifier;
    private String password;
    
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

class AuthResponse {
    private String token;
    private String role;
    private String name;
    private String email;
    public AuthResponse(String token, String role, String name, String email) { 
        this.token = token; 
        this.role = role; 
        this.name = name;
        this.email = email;
    }
    public String getToken() { return token; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getEmail() { return email; }
}