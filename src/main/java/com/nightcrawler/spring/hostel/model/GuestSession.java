package com.nightcrawler.spring.hostel.model;

import lombok.Data;

/**
 * Simple session-scoped model to hold the logged-in guest identity.
 */
@Data
public class GuestSession {
    private String email;
    private String name;

    public boolean isLoggedIn() {
        return email != null && !email.isBlank();
    }
}
