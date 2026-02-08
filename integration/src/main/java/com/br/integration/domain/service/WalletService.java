package com.br.integration.domain.service;
import com.br.integration.config.security.TokenService;
import com.br.integration.domain.exception.userexception.UserException;
import com.br.integration.domain.exception.walletexception.WalletException;
import com.br.integration.domain.entites.User;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.repository.UserRepository;
import com.br.integration.domain.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class WalletService {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletRepository walletRepository;
        public Wallet getWallet(String token){
            if(token.startsWith("Bearer")){
                token = token.substring(6).trim();
            }
            String email = tokenService.validateToken(token);

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserException("User not Found!"));

            Wallet wallet = walletRepository.findByUsers(user)
                    .orElseThrow(() -> new WalletException("Wallet not Found!"));


            return wallet;

        }

}
