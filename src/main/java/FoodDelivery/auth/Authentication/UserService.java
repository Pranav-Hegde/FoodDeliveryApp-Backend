package FoodDelivery.auth.Authentication;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @jakarta.annotation.PostConstruct
    public void initAdmin() {
        if (userRepository.findByEmail("admin").isEmpty()) {
            User admin = new User("admin", "ROLE_SUPER_ADMIN", "Super Admin", "9999999999", "pranav@123");
            userRepository.save(admin);
        }
    }

    public String getRoleByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getRole)
                .orElse(null);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    public User registerUser(String email, String phone, String name, String password, String role) {
        User user = new User(email, role, name, phone, password);
        return userRepository.save(user);
    }

    public String createUser(String email, String role) {
        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        userRepository.save(user);
        return role;
    }
}