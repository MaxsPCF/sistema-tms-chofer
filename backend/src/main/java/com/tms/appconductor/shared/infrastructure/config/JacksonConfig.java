package com.tms.appconductor.shared.infrastructure.config;

// import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 
 * Configuración de Jackson para Spring Boot 4.0.7
 * 
 * 
 * 
 * NOTA: En Spring Boot 4.x, Jackson2ObjectMapperBuilderCustomizer
 * 
 * fue reemplazado. Se configura directamente el ObjectMapper como bean.
 * 
 */
@Configuration

public class JacksonConfig {
    /**
     * 
     * ObjectMapper principal para toda la aplicación.
     * 
     * Spring Boot 4.x detecta automáticamente este bean
     * 
     * y lo usa en todos los codecs HTTP.
     * 
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                // Módulo para Java 8+ time (LocalDateTime, ZonedDateTime, etc.)
                .addModule(new JavaTimeModule())
                // Serialización
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)

                // Deserialización
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)

                // Configuración visual
                // .serializationInclusion(JsonInclude.Include.NON_NULL)

                // Formato de fecha ISO 8601
                .defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
                .defaultTimeZone(TimeZone.getTimeZone("UTC"))
                .build();
    }
}