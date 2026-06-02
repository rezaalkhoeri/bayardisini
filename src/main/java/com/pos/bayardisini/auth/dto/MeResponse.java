package com.pos.bayardisini.auth.dto;

import java.util.UUID;

public record MeResponse(
        UUID id,
        UUID tenantId,
        String fullName,
        String email) {
}