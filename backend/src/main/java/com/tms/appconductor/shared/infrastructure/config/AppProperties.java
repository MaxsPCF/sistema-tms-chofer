package com.tms.appconductor.shared.infrastructure.config;

import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Configuration;

import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "app")

public class AppProperties {
    private Jwt jwt = new Jwt();
    private Security security = new Security();

    @Data
    public static class Jwt {
        @NotBlank
        private String secret;

        @Min(60000)
        private long expirationMs = 3600000;

        @Min(60000)
        private long refreshExpirationMs = 86400000;

        private String issuer = "api-conductor-transporte-api";
    }

    @Data
    public static class Security {
        private Bcrypt bcrypt = new Bcrypt();

        @Min(3)
        private int maxFailedAttempts = 5;

        @Min(5)
        private int lockDurationMinutes = 30;

        private Password password = new Password();

        @Data
        public static class Bcrypt {
            @Min(4)
            private int rounds = 12;
        }

        @Data
        public static class Password {
            @Min(6)
            private int minLength = 8;
            private boolean requireUppercase = true;
            private boolean requireLowercase = true;
            private boolean requireDigit = true;
            private boolean requireSpecial = true;
        }
    }
}