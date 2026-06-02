package com.pos.bayardisini.auth.dto;

public record RegisterRequest(
        String tenantId,
        String fullName,
        String email,
        String phone,
        String password
) {
}