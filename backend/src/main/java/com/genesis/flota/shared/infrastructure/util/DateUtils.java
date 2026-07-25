package com.genesis.flota.shared.infrastructure.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
        // Utility class
    }

    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final ZoneId LIMA = ZoneId.of("America/Lima");

    public static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static LocalDateTime now() {
        return LocalDateTime.now(UTC);
    }

    public static LocalDateTime nowPeru() {
        return LocalDateTime.now(LIMA);
    }

    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(ISO_FORMATTER);
    }

    public static LocalDateTime parse(String dateTimeStr) {
        if (dateTimeStr == null) return null;
        return LocalDateTime.parse(dateTimeStr, ISO_FORMATTER);
    }

    public static boolean isBefore(LocalDateTime date1, LocalDateTime date2) {
        return date1 != null && date2 != null && date1.isBefore(date2);
    }

    public static boolean isAfter(LocalDateTime date1, LocalDateTime date2) {
        return date1 != null && date2 != null && date1.isAfter(date2);
    }
}