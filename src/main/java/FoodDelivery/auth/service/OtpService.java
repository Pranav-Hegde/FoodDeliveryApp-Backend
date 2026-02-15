package FoodDelivery.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private static final int OTP_EXPIRY_MINUTES = 5;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Generate and store OTP
    public String generateOtp(String email) {


        String otp = String.valueOf(
                100000 + new Random().nextInt(900000)
        );

        String key = "OTP:" + email;

        redisTemplate.opsForValue()
                .set(key, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);

        return otp;
    }

    // Verify OTP (NO HTTP CODE HERE)
    public boolean verifyOtp(String email, String otp) {

        String key = "OTP:" + email;
        String savedOtp = redisTemplate.opsForValue().get(key);

        if (savedOtp == null) {
            return false;
        }

        if (savedOtp.equals(otp)) {
            redisTemplate.delete(key); // one-time use
            return true;
        }

        return false;
    }
}
