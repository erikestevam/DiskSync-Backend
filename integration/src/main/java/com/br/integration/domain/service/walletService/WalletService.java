package com.br.integration.domain.service.walletService;

import com.br.integration.domain.dto.RechargeDTO;
import com.br.integration.domain.dto.WalletDTO;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.exception.walletexception.WalletException;
import com.br.integration.domain.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.br.integration.config.security.TokenService;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TokenService tokenService;

    public WalletService(WalletRepository walletRepository, TokenService tokenService) {
        this.walletRepository = walletRepository;
        this.tokenService = tokenService;
    }

    public Wallet getWallet(String token) {
        String cleanToken = token.replace("Bearer ", "");

        String email;
        try {
            email = tokenService.validateToken(cleanToken);
        } catch (RuntimeException e) {
            throw new WalletException("Token inválido ou expirado: " + e.getMessage());
        }

        return walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new WalletException("Carteira não encontrada para o usuário"));
    }

    @Transactional
    public WalletDTO rechargeAndReturnDTO(String token, RechargeDTO dto) {
        Wallet wallet = getWallet(token);

        BigDecimal newBalance = wallet.getBalance().add(dto.amount());
        wallet.setBalance(newBalance);
        wallet.setLastUpdate(LocalDateTime.now());

        Wallet updated = walletRepository.save(wallet);

        return new WalletDTO(
                updated.getBalance(),
                updated.getLastUpdate()
        );
    }
}