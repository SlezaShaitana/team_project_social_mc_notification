package com.social.mcnotification.security;

public class CustomUserDetails {
    private final String userId;

    public CustomUserDetails(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
