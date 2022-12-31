package com.example.springbootecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests(configurer ->
            configurer.antMatchers("/api/orders/**")
                    .authenticated())
                .oauth2ResourceServer()
                .jwt();
        httpSecurity.cors();
        httpSecurity.csrf().disable();
        return httpSecurity.build();
    }
}
