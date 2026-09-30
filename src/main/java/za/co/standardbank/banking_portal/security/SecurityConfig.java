package za.co.standardbank.banking_portal.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                //disabling csrf
                .csrf(csrf -> csrf.disable())
                //use our cors coniguration
                .cors(cors -> {})
                // We are using JWTs rather than server-side sessions.
                //Don't create or maintain server-side login sessions.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        ))

                .authorizeHttpRequests(auth -> auth
                        // Login and registration don't require authentication.
                        .requestMatchers("/api/auth/**").permitAll()

                        // Only administrators can access admin endpoints.
                        .requestMatchers("/api/admin/**", "/api/customers/**")
                        .hasRole("ADMIN")

                        // Everything else requires authentication.
                        .anyRequest().authenticated()
                )

                // Check for a JWT before Spring's normal
                // username/password authentication filter.
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // Keep Basic Auth available for our current Postman testing.
                .httpBasic(httpBasic -> {});

        return http.build();
    }
}