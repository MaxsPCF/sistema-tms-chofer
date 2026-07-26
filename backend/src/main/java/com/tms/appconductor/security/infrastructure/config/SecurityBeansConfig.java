package com.tms.appconductor.security.infrastructure.config;

import com.tms.appconductor.security.infrastructure.adapter.out.security.NetCompatibleBCryptPasswordEncoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeansConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new NetCompatibleBCryptPasswordEncoder();
    }
}