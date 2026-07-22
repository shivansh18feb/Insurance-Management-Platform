package com.insurance.backend.util;

public final class StringUtils {

    private StringUtils() {
    }

    public static String normalize(String value) {

        return value == null
                ? null
                : value.strip();
    }

    public static String normalizeEmail(String email) {

        return email == null
                ? null
                : email.strip().toLowerCase();
    }
}