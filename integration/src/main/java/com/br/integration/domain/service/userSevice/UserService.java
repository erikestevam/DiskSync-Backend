package com.br.integration.domain.service.userSevice;

import com.br.integration.config.security.AuthenticationService;
import com.br.integration.domain.dto.UserDTO;
import com.br.integration.domain.entites.User;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.repository.UserRepository;
import com.br.integration.domain.exception.userexception.UserException;
import com.br.integration.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    private final AuthenticationService authenticationService;

    UserDetails userDetails;
    private final PasswordEncoder passwordEncoder;

    public User create(User user){

        if(this.usersRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserException("Email " + user.getEmail() + " já está cadastrado no sistema.");
        }
        user = user.toBuilder().password(this.passwordEncoder.encode(user.getPassword())).build();
        usersRepository.save(user);
        Wallet wallet = new Wallet(BigDecimal.ZERO, 0L, LocalDateTime.now(),user);
        walletRepository.save(wallet);
        return user;
    }
    public User updateUser(String email, User user) {
        String currentUserEmail = authenticationService.getCurrentUserEmail();
        
        if (!currentUserEmail.equals(email)) {
            throw new UserException("Você não tem permissão para alterar este usuário.");
        }

        User existingUser = this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuário com email " + email + " não foi encontrado."));

        if(!email.equals(user.getEmail()) && this.usersRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserException(user.getEmail() + " já está em uso por outro usuário.");
        }

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser = existingUser.toBuilder().password(this.passwordEncoder.encode(existingUser.getPassword())).build();

        return this.usersRepository.save(existingUser);
    }

    public ResponseEntity<?> deleteUser(String email) {
        String currentUserEmail = authenticationService.getCurrentUserEmail();
        
        if (!currentUserEmail.equals(email)) {
            throw new UserException("Você não tem permissão para deletar este usuário.");
        }
        
        Optional<User> usuarioOptional = usersRepository.findByEmail(email);
        if (usuarioOptional.isEmpty()) {
            throw new UserException("Usuário com email " + email + " não foi encontrado.");
        }
        User user = usuarioOptional.get();
        Optional<Wallet> walletOptional =  walletRepository.findByUser(user);

        if (walletOptional.isEmpty()) {
            throw new UserException("Carteira do usuário " + email + " não foi encontrada.");
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
