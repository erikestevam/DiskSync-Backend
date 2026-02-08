package com.br.integration.domain.service;

import com.br.integration.domain.entites.User;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.repository.UserRepository;
import com.br.integration.domain.exception.userexception.UserException;
import com.br.integration.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository usersRepository;
    @Autowired
    private final WalletRepository walletRepository;

    UserDetails userDetails;
    private final PasswordEncoder passwordEncoder;

    public User create(User user){

        if(this.usersRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserException(user.getEmail()+ " User already exists ");
        }
        user = user.toBuilder().password(this.passwordEncoder.encode(user.getPassword())).build();
        usersRepository.save(user);
        Wallet wallet = new Wallet(BigDecimal.ZERO, 0L, LocalDateTime.now(),user);
        walletRepository.save(wallet);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optional = usersRepository.findByEmail(email);
        UserDetails userDetails = optional.get();;
         return  userDetails;
    }
}
