package com.br.integration.domain.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CartDTO(
        String userEmail,
        List<String> albumIds,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}