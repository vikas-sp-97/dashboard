package com.example.dashboard.enums;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public enum RoleEnum {
    USER("User"),
    ADMIN("Admin"),
    CLIENT_USER("Client_user");

    private String role;

    RoleEnum(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
}
