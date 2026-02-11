package com.br.integration.domain.dto;



import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletDTO(
        BigDecimal balance,
        LocalDateTime lastUpdate
) {}
