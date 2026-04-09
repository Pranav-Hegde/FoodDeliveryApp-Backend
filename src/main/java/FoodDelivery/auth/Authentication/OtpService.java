package FoodDelivery.auth.Authentication;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;

    public OtpService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ✅ Generate OTP
    public String generateOtp(String email) {
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        redisTemplate.opsForValue()
                .set("OTP:" + email, otp, Duration.ofMinutes(5)); // TTL

        return otp;
    }

    // ✅ Verify OTP
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue()
                .get("OTP:" + email);

        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete("OTP:" + email); // one-time use
            return true;
        }
        return false;
    }
}