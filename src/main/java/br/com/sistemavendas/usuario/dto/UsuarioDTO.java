package br.com.sistemavendas.usuario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    @NotEmpty(message = "{campo.login.obrigatorio}")
    private String login;

    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
}
