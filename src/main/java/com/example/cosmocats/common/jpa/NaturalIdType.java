package com.example.cosmocats.common.jpa;

import java.security.SecureRandom;
import java.time.Year;
import java.util.function.Supplier;

public enum NaturalIdType {
    ORDER(() -> {
        String year = Year.now().toString().substring(2);
        String random = generateSafeToken(6);
        return String.format("ORD-%s-%s", year, random);
    });

    private final Supplier<String> generator;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALPHABET = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    NaturalIdType(Supplier<String> generator) {
        this.generator = generator;
    }

    public String generate() {
        return generator.get();
    }

    private static String generateSafeToken(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHABET.charAt(SECURE_RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
