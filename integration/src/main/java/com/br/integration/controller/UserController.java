package com.br.integration.controller;

import com.br.integration.domain.dto.AuthDTO;
import com.br.integration.domain.dto.LoginResponseDTO;
import com.br.integration.config.security.TokenService;
import com.br.integration.domain.exception.userexception.UserException;
import com.br.integration.domain.entites.User;
import com.br.integration.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


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
                    return  new ResponseEntity<>(HttpStatus.CREATED);
               }catch(UserException e){
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
        @GetMapping("/teste")
        public ResponseEntity<String> testeController(){
            return ResponseEntity.ok("teste -  verificação de rota de autenticação");
        }
}
