package FoodDelivery.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
        FilterRegistrationBean<JwtAuthFilter> registrationBean =
                new FilterRegistrationBean<>();

        registrationBean.setFilter(new JwtAuthFilter());
        registrationBean.addUrlPatterns("/api/*"); // protect APIs

        return registrationBean;
    }
}
