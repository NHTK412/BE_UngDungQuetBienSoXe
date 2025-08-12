package com.example.licenseplate.config;

import com.example.licenseplate.filter.JwtAuthenticationFilter;
import com.example.licenseplate.filter.WhitelistFilter;
import com.example.licenseplate.service.AuthEntryPointJwt;
import com.example.licenseplate.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt authEntryPointJwt;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final WhitelistFilter whitelistFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
            AuthEntryPointJwt authEntryPointJwt,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            WhitelistFilter whitelistFilter) {
        this.userDetailsService = userDetailsService;
        this.authEntryPointJwt = authEntryPointJwt;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.whitelistFilter = whitelistFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:8087", "http://localhost:3001"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cache-Control"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Điều chỉnh chỗ này _____________________________________________
                        // Các endpoint public được phép truy cập
                        .requestMatchers("/api/auth/signin", "/api/auth/signup", "/api/auth/login-history/**",
                                "/api/camera/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/accidents").permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .anyRequest().authenticated());
        // Điều chỉnh chỗ này _____________________________________________

        http.addFilterBefore(whitelistFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(jwtAuthenticationFilter, WhitelistFilter.class);

        return http.build();
    }
}
