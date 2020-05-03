package br.com.sistemavendas.usuario.controller;

import br.com.sistemavendas.usuario.entity.Usuario;
import br.com.sistemavendas.security.jwt.JwtService;
import br.com.sistemavendas.usuario.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
//@ActiveProfiles("test")
@ContextConfiguration(classes = {UsuarioController.class, UsuarioServiceImpl.class
        ,PasswordEncoder.class, JwtService.class, Usuario.class})
@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc
public class UsuarioControllerTests {

    private static final String api_usuario = "/api/usuarios";

    @Autowired
    private MockMvc mvc;

   // @Test
    @DisplayName("Deve cadastrar usuario.")
    @WithMockUser
    public void cadastrarUsuarioTest() throws Exception{
        //cenario

        //execucao

        //verificacao
    }

    public Usuario prepararUsuario(){
        return new Usuario();
    }

}
