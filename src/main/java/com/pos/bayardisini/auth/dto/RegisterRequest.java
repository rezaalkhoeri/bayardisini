package com.pos.bayardisini.auth.dto;

public record RegisterRequest(
        String fullName,
        String email,
        String password
) {
}