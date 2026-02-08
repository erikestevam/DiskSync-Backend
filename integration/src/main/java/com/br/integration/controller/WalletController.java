package com.br.integration.controller;

import com.br.integration.domain.exception.walletexception.WalletException;
import com.br.integration.domain.dto.WalletDTO;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("wallet/")
public class WalletController {
    @Autowired
        WalletService walletService;
        @GetMapping("mywallet")
        public ResponseEntity<?> wallet(@RequestHeader("Authorization") String token ){
            try{
            Wallet wallet = walletService.getWallet(token);
                WalletDTO walletDTO = new WalletDTO(wallet.getBalance(),wallet.getPoints(),wallet.getLastUpdate());
                return ResponseEntity.ok(walletDTO);
            }catch(WalletException e){
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FOUND);
        }
    }
}
