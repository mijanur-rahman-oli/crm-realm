package com.crmrealm.leadtracker;
public record Lead(String name, String email, int score) {

    public static final int MIN_SCORE = 0;
    public static final int MAX_SCORE = 100;

    public Lead {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name must not be empty.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be empty.");
        }
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw new IllegalArgumentException(
                    "Score must be between " + MIN_SCORE + " and " + MAX_SCORE + " (got " + score + ").");
        }
        name = name.trim();
        email = email.trim();
    }
}
