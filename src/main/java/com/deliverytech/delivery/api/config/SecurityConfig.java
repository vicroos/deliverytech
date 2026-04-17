package com.deliverytech.delivery.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.deliverytech.delivery.api.Security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@EnableMethodSecurity
@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtfilter;

    public SecurityConfig(JwtAuthenticationFilter jwtfilter) {
        this.jwtfilter = jwtfilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
            .headers(headers -> headers.frameOptions(f -> f.disable()))
            .sessionManagement(sm -> 
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex.authenticationEntryPoint((req, res, e) ->
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
            ))
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api-docs/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/restaurantes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/restaurantes/**").hasAnyRole("ADMIN", "RESTAURANTE")
                .requestMatchers(HttpMethod.GET, "/produtos/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/produtos/**").hasAnyRole("ADMIN", "RESTAURANTE")
                .requestMatchers(HttpMethod.PATCH, "/produtos/**").hasAnyRole("ADMIN", "RESTAURANTE")
                .requestMatchers(HttpMethod.GET, "/clients/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/clientes/cadastrar").hasRole("CLIENTE")
                .anyRequest().authenticated())

            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration() {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(jwtfilter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}