package FoodDelivery.auth.controller;

import FoodDelivery.auth.security.JwtUtil;
import FoodDelivery.auth.service.OtpService;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private static final Logger logger = Logger.getLogger(AuthController.class.getName());

    @Autowired
    private OtpService otpService;

    @PostMapping("/request-otp")
    public ResponseEntity<?> requestOtp(@RequestParam String email) {
        try {
            String otp = otpService.generateOtp(email);

            System.out.println("OTP for " + email + " = " + otp); // add this

            logger.info("OTP sent successfully");
            return ResponseEntity.ok("OTP sent successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP");
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp) {
        try {
            boolean valid = otpService.verifyOtp(email, otp);

            if (!valid) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid or expired OTP");
            }

            String token = JwtUtil.generateToken(email);
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong. Please try again.");
        }
    }
}
