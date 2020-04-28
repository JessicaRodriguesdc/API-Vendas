package br.com.sistemavendas.rest.controller;

import br.com.sistemavendas.domain.entity.Cliente;
import br.com.sistemavendas.domain.entity.Usuario;
import br.com.sistemavendas.rest.dto.ClienteDTO;
import br.com.sistemavendas.security.jwt.JwtService;
import br.com.sistemavendas.service.ClienteService;
import br.com.sistemavendas.service.impl.ClienteServiceImpl;
import br.com.sistemavendas.service.impl.UsuarioServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.thymeleaf.spring5.expression.Mvc;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {ClienteController.class, ClienteService.class
        , PasswordEncoder.class, JwtService.class, ClienteDTO.class , ModelMapper.class})
@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc
public class ClienteControllerTests {

    private static final String api_cliente = "/api/clientes";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClienteService service;

    @Test
    @DisplayName("Deve cadastrar cliente.")
    @WithMockUser
    public void cadastrarClienteTest() throws Exception{
        //cenario
        Integer id= 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();
        BDDMockito.given(service.salvar(Mockito.any(Cliente.class)))
                .willReturn(clienteFake);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(api_cliente)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(clienteFake.getId()))
                .andExpect(jsonPath("nome").value(clienteFake.getNome()))
                .andExpect(jsonPath("cpf").value(clienteFake.getCpf()));

    }

    @Test
    @DisplayName("Deve buscar o cliente pelo id.")
    @WithMockUser
    public void getClienteByIdTest() throws Exception{
        //cenario
        Integer id = 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        BDDMockito.given(service.obterCliente(id))
                .willReturn(Optional.of(clienteFake));

        //execulcao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(api_cliente+"/"+id)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(clienteFake.getId()))
                .andExpect(jsonPath("nome").value(clienteFake.getNome()))
                .andExpect(jsonPath("cpf").value(clienteFake.getCpf()));
    }

    public ClienteDTO prepararClienteDTO(){
        return new ClienteDTO("jessica","93229667000");
    }

}
