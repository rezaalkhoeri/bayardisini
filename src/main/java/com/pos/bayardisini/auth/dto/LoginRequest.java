package com.pos.bayardisini.auth.dto;

public record LoginRequest(
        String email,
        String password
) {
}