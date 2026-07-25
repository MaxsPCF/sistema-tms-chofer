package com.genesis.flota.security.infrastructure.adapter.out.security;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.genesis.flota.security.application.port.out.PasswordEncoderPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component("netCompatiblePasswordEncoder")
public class NetCompatibleBCryptPasswordEncoder implements PasswordEncoder, PasswordEncoderPort {

    private static final int BCRYPT_COST = 12;

    @Override
    public String encode(CharSequence rawPassword) {
        // SHA-384 → Base64 → BCrypt (COMPATIBLE CON .NET EnhancedHashPassword)
        String sha384Base64 = sha384Base64(rawPassword.toString());
        byte[] bcryptResult = BCrypt.with(BCrypt.Version.VERSION_2A)
                .hash(BCRYPT_COST, sha384Base64.toCharArray());
        return new String(bcryptResult, StandardCharsets.UTF_8);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return false;
        }

        // SHA-384 → Base64 → BCrypt.verify (COMPATIBLE CON .NET EnhancedVerify)
        String sha384Base64 = sha384Base64(rawPassword.toString());

        BCrypt.Result result = BCrypt.verifyer(BCrypt.Version.VERSION_2A)
                .verify(sha384Base64.toCharArray(), encodedPassword.toCharArray());

        log.debug("Verificación: {}", result.verified ? "EXITOSA" : "FALLIDA");
        return result.verified;
    }

    @Override
    public Mono<String> encodeReactive(String rawPassword) {
        return Mono.fromCallable(() -> encode(rawPassword));
    }

    @Override
    public Mono<Boolean> matchesReactive(String rawPassword, String encodedPassword) {
        return Mono.fromCallable(() -> matches(rawPassword, encodedPassword));
    }

    @Override
    public Mono<Boolean> needsRehash(String encodedPassword) {
        return Mono.just(encodedPassword != null &&
                !encodedPassword.startsWith("$2a$" + BCRYPT_COST + "$"));
    }

    /**
     * Calcula SHA-384 y lo codifica en Base64.
     * IDÉNTICO a como lo hace .NET BCrypt.Net.EnhancedHashPassword internamente.
     */
    private String sha384Base64(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-384");
            byte[] sha384 = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(sha384);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-384 not available", e);
        }
    }
}