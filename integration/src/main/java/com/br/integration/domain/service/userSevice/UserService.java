package com.br.integration.domain.service.userSevice;

import com.br.integration.config.security.AuthenticationService;
import com.br.integration.domain.dto.CreateUserDTO;
import com.br.integration.domain.dto.UpdateUserDTO;
import com.br.integration.domain.dto.UserDTO;
import com.br.integration.domain.entites.Cart;
import com.br.integration.domain.entites.Order;
import com.br.integration.domain.entites.User;
import com.br.integration.domain.entites.Wallet;
import com.br.integration.domain.repository.CartRepository;
import com.br.integration.domain.repository.OrderRepository;
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
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final OrderRepository orderRepository;
    private final AuthenticationService authenticationService;

    UserDetails userDetails;
    private final PasswordEncoder passwordEncoder;

    public User create(CreateUserDTO createUserDTO){

        if(this.usersRepository.findByEmail(createUserDTO.email()).isPresent()){
            throw new UserException("Email " + createUserDTO.email() + " já está cadastrado no sistema.");
        }
        
        User user = User.builder()
                .name(createUserDTO.name())
                .email(createUserDTO.email())
                .password(this.passwordEncoder.encode(createUserDTO.password()))
                .build();
        
        user = usersRepository.save(user);
        Wallet wallet = new Wallet(BigDecimal.ZERO, LocalDateTime.now(), user);
        walletRepository.save(wallet);
        return user;
    }
    public User updateUser(String email, UpdateUserDTO updateUserDTO) {
        String currentUserEmail = authenticationService.getCurrentUserEmail();
        
        if (!currentUserEmail.equals(email)) {
            throw new UserException("Você não tem permissão para alterar este usuário.");
        }

        User existingUser = this.usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuário com email " + email + " não foi encontrado."));

        if(!email.equals(updateUserDTO.email()) && this.usersRepository.findByEmail(updateUserDTO.email()).isPresent()){
            throw new UserException(updateUserDTO.email() + " já está em uso por outro usuário.");
        }

        existingUser.setName(updateUserDTO.name());
        existingUser.setEmail(updateUserDTO.email());
        existingUser = existingUser.toBuilder()
                .password(this.passwordEncoder.encode(updateUserDTO.password()))
                .build();

        return this.usersRepository.save(existingUser);
    }

    public ResponseEntity<?> deleteUser(String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Usuário com email " + email + " não foi encontrado."));
        
        String currentUserEmail = authenticationService.getCurrentUserEmail();
        if (!currentUserEmail.equals(email)) {
            throw new UserException("Você não tem permissão para deletar este usuário.");
        }
        
        log.info("Iniciando exclusão do usuário: {}", email);
        
        List<Order> orders = orderRepository.findByUserEmail(email);
        if (!orders.isEmpty()) {
            orderRepository.deleteAll(orders);
            log.info("Deletados {} pedido(s) do usuário {}", orders.size(), email);
        }
        
        Optional<Cart> cartOptional = cartRepository.findByUserEmail(email);
        if (cartOptional.isPresent()) {
            cartRepository.delete(cartOptional.get());
            log.info("Carrinho do usuário {} deletado", email);
        }
        
        Optional<Wallet> walletOptional = walletRepository.findByUser(user);
        if (walletOptional.isPresent()) {
            walletRepository.delete(walletOptional.get());
            log.info("Carteira do usuário {} deletada", email);
        }
        
        usersRepository.delete(user);
        log.info("Usuário {} deletado com sucesso", email);
        
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
