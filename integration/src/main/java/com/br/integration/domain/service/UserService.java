package com.br.integration.domain.service;

import com.br.integration.domain.dto.UserDTO;
import com.br.integration.domain.entites.User;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.repository.UserRepository;
import com.br.integration.domain.exception.userexception.UserException;
import com.br.integration.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    public User updateUser(String email, User user) {

        if(this.usersRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserException(user.getEmail()+ "Email já está em uso por um usuário!");
        }
        User existingUser = this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuário não encontrado"));

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser = existingUser.toBuilder().password(this.passwordEncoder.encode(existingUser.getPassword())).build();

        return this.usersRepository.save(existingUser);
    }

    public ResponseEntity<?> deleteUser(String email) {
        Optional<User> usuarioOptional = usersRepository.findByEmail(email);
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = usuarioOptional.get();
        Optional<Wallet> walletOptional =  walletRepository.findByUsers(user);

        if (walletOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Wallet wallet = walletOptional.get();
        walletRepository.delete(wallet);
        usersRepository.delete(user);
        return ResponseEntity.noContent().build();
    }
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> users = this.usersRepository.findAll().stream()
                .map(user -> new UserDTO(user.getName(),user.getUsername()))
                .toList();

        return ResponseEntity.ok(users);
    }
    @Override
    public  UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optional = usersRepository.findByEmail(email);
        UserDetails userDetails = optional.get();;
         return  userDetails;
    }
}
