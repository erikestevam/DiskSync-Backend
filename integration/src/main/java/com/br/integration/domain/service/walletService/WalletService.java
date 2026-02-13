package com.br.integration.domain.service.walletService;

import com.br.integration.config.security.AuthenticationService;
import com.br.integration.domain.dto.RechargeDTO;
import com.br.integration.domain.dto.WalletDTO;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.exception.walletexception.WalletException;
import com.br.integration.domain.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final AuthenticationService authenticationService;

    public WalletService(WalletRepository walletRepository, AuthenticationService authenticationService) {
        this.walletRepository = walletRepository;
        this.authenticationService = authenticationService;
    }

    public Wallet getWallet() {
        String email = authenticationService.getCurrentUserEmail();
        return walletRepository.findByUserEmail(email)
                .orElseThrow(() -> new WalletException("Carteira não encontrada para o usuário"));
    }

    @Transactional
    public WalletDTO rechargeAndReturnDTO(RechargeDTO dto) {
        Wallet wallet = getWallet();

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