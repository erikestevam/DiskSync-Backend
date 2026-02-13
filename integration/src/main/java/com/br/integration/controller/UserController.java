package com.br.integration.controller;

import com.br.integration.domain.dto.AuthDTO;
import com.br.integration.domain.dto.LoginResponseDTO;
import com.br.integration.config.security.TokenService;
import com.br.integration.domain.dto.UserDTO;
import com.br.integration.domain.exception.userexception.UserException;
import com.br.integration.domain.entites.User;
import com.br.integration.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
        @PostMapping("/save")
        public ResponseEntity<?> save(@RequestBody User user){
               try{
                    usersService.create(user);
                    return  new ResponseEntity<>("Usuário criado com sucesso.", HttpStatus.CREATED);
               }catch(UserException e){
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
               }
        }
        @PutMapping("update/{email}")
        public ResponseEntity<?> updateUser(@PathVariable String email, @RequestBody User user) {
            try {
                usersService.updateUser(email, user);
                return  new ResponseEntity<>("Usuário atualizado com sucesso.", HttpStatus.OK);
            } catch (UserException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }
        @DeleteMapping("delete/{email}")
        public ResponseEntity<?> deleteUser(@PathVariable String email) {
            try {
                usersService.deleteUser(email);
                return  new ResponseEntity<>("Usuário deletado com sucesso.", HttpStatus.OK);
            } catch (UserException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
            }
        }

    @PostMapping("/auth")
        public ResponseEntity<?> auth(@RequestBody AuthDTO authDTO){
              var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.email(),authDTO.password());
              var auth = authenticationManager.authenticate(usernamePassword);
              var token = tokenService.generateToken((User) auth.getPrincipal());
              return ResponseEntity.ok(new LoginResponseDTO(token));
        }
        @GetMapping("/list")
        public ResponseEntity<List<UserDTO>> listUsers(){
            return usersService.listUsers();
        }
}
