package com.br.integration.controller;

import com.br.integration.domain.dto.AuthDTO;
import com.br.integration.domain.dto.CreateUserDTO;
import com.br.integration.domain.dto.UpdateUserDTO;
import com.br.integration.domain.dto.LoginResponseDTO;
import com.br.integration.config.security.TokenService;
import com.br.integration.domain.dto.UserDTO;
import com.br.integration.domain.exception.userexception.UserException;
import com.br.integration.domain.entites.User;
import com.br.integration.domain.service.userSevice.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuário", description = "Cadastro, atualização, exclusão, autenticação e listagem de usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
        @Autowired
        private AuthenticationManager authenticationManager;
        @Autowired
        private TokenService tokenService;
        @Autowired
        private UserService usersService;

        @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário")
        @PostMapping("/save")
        public ResponseEntity<?> save(@RequestBody CreateUserDTO createUserDTO){
               try{
                    usersService.create(createUserDTO);
                    return  new ResponseEntity<>("Usuário criado com sucesso.", HttpStatus.CREATED);
               }catch(UserException e){
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
               }
        }

        @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente pelo e-mail")
        @PutMapping("update/{email}")
        public ResponseEntity<?> updateUser(@Parameter(description = "E-mail do usuário") @PathVariable String email, @RequestBody UpdateUserDTO updateUserDTO) {
            try {
                usersService.updateUser(email, updateUserDTO);
                return  new ResponseEntity<>("Usuário atualizado com sucesso.", HttpStatus.OK);
            } catch (UserException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }

        @Operation(summary = "Excluir usuário", description = "Remove um usuário pelo e-mail")
        @DeleteMapping("delete/{email}")
        public ResponseEntity<?> deleteUser(@Parameter(description = "E-mail do usuário") @PathVariable String email) {
            try {
                usersService.deleteUser(email);
                return  new ResponseEntity<>("Usuário deletado com sucesso.", HttpStatus.OK);
            } catch (UserException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }

        @Operation(summary = "Autenticar", description = "Gera token JWT para o usuário (login)")
        @PostMapping("/auth")
        public ResponseEntity<?> auth(@RequestBody AuthDTO authDTO){
              var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.email(),authDTO.password());
              var auth = authenticationManager.authenticate(usernamePassword);
              var token = tokenService.generateToken((User) auth.getPrincipal());
              return ResponseEntity.ok(new LoginResponseDTO(token));
        }

        @Operation(summary = "Listar usuários", description = "Retorna a lista de todos os usuários")
        @GetMapping("/list")
        public ResponseEntity<List<UserDTO>> listUsers(){
            return usersService.listUsers();
        }
}
