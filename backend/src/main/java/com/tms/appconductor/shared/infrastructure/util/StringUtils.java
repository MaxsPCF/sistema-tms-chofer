package com.tms.appconductor.shared.infrastructure.util;

import java.util.UUID;

public final class StringUtils {

    private StringUtils() {
        // Utility class
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotNullOrEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static String truncate(String str, int maxLength) {
        if (str == null)
            return null;

        return str.length() > maxLength ? str.substring(0, maxLength) : str;
    }

    public static String generateShortUuid() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String maskEmail(String email) {
        if (email == null || !email.contains("@"))
            return email;

        String[] parts = email.split("@");
        String name = parts[0];

        if (name.length() <= 2)
            return email;

        return name.charAt(0) + "***" + name.charAt(name.length() - 1) + "@" + parts[1];
    }

    public static String capitalize(String str) {
        if (isNullOrEmpty(str))
            return str;

        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}