package com.br.integration.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização de um usuário")
public record UpdateUserDTO(
        @Schema(description = "Nome do usuário", example = "Usuario teste")
        String name,
        
        @Schema(description = "E-mail do usuário", example = "teste@gmail.com")
        String email,
        
        @Schema(description = "Senha do usuário", example = "321")
        String password
) { }
