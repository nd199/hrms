package com.naren.authservice.dto;

import java.time.LocalDateTime;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        LocalDateTime expiresAt
) {
}
