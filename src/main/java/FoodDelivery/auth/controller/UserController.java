package FoodDelivery.auth.controller;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        logger.info("Logged in as :"+email);
        return "Logged in as: " + email;
    }
}