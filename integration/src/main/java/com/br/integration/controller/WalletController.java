package com.br.integration.controller;

import com.br.integration.domain.dto.RechargeDTO;
import com.br.integration.domain.dto.WalletDTO;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.exception.walletexception.WalletException;
import com.br.integration.domain.service.walletService.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "Carteira", description = "Consultar saldo e recarregar carteira do usuário")
@RestController
@RequestMapping("/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @Operation(summary = "Minha carteira", description = "Retorna o saldo e dados da carteira do usuário (requer Authorization)")
    @GetMapping("/mywallet")
    public ResponseEntity<?> getMyWallet(@Parameter(description = "Bearer token") @RequestHeader("Authorization") String token) {
        try {
            Wallet wallet = walletService.getWallet(token);
            WalletDTO dto = new WalletDTO(
                    wallet.getBalance(),
                    wallet.getLastUpdate()
            );
            return ResponseEntity.ok(dto);
        } catch (WalletException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Recarregar carteira", description = "Adiciona saldo à carteira do usuário (requer Authorization)")
    @PostMapping("/recharge")
    public ResponseEntity<?> rechargeBalance(
            @Parameter(description = "Bearer token") @RequestHeader("Authorization") String token,
            @RequestBody RechargeDTO rechargeDTO) {

        if (rechargeDTO == null || rechargeDTO.amount() == null) {
            return ResponseEntity.badRequest().body("O valor da recarga não pode ser vazio");
        }
        if (rechargeDTO.amount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("O valor da recarga deve ser maior que zero");
        }

        try {
            WalletDTO updatedWallet = walletService.rechargeAndReturnDTO(token, rechargeDTO);
            return ResponseEntity.ok(updatedWallet);
        } catch (WalletException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar recarga: " + e.getMessage());
        }
    }
}