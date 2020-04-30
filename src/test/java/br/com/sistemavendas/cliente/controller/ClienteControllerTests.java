package br.com.sistemavendas.cliente.controller;

import br.com.sistemavendas.cliente.controller.ClienteController;
import br.com.sistemavendas.cliente.entity.Cliente;
import br.com.sistemavendas.cliente.dto.ClienteDTO;
import br.com.sistemavendas.security.jwt.JwtService;
import br.com.sistemavendas.cliente.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {ClienteController.class, ClienteService.class
        ,PasswordEncoder.class, JwtService.class, ClienteDTO.class ,ModelMapper.class})
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

        BDDMockito.given(service.obterCliente(Mockito.anyInt()))
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

    @Test
    @DisplayName("Deve atualizar um cliente existente.")
    @WithMockUser
    public void atualizarClienteTest() throws Exception{
        //cenario
        Integer id = 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome("jessica rodrigues")
                .cpf(dto.getCpf())
                .build();

        BDDMockito.given(service.obterCliente(Mockito.anyInt()))
                .willReturn(Optional.of(clienteFake));
        BDDMockito.given(service.atualizar(Mockito.anyInt(),Mockito.any(Cliente.class)))
                .willReturn(clienteFake);

        String json = new ObjectMapper().writeValueAsString(dto);

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(api_cliente+"/"+id)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("id").value(clienteFake.getId()))
                .andExpect(jsonPath("nome").value(clienteFake.getNome()))
                .andExpect(jsonPath("cpf").value(clienteFake.getCpf()));
    }

    @Test
    @DisplayName("Deve deletar um cliente existente.")
    @WithMockUser
    public void deletarClienteTest() throws Exception{
        //cenario
        Integer id = 1;
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(id)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();


        BDDMockito.given(service.obterCliente(Mockito.anyInt()))
                .willReturn(Optional.of(clienteFake));

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(api_cliente+"/"+id)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar clientes.")
    @WithMockUser
    public void listarClienteTest() throws Exception{
        //cenario
        ClienteDTO dto = prepararClienteDTO();
        Cliente clienteFake = Cliente.builder()
                .id(1)
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .build();

        List<Cliente> ListaClientesFakes = new ArrayList<Cliente>();
        ListaClientesFakes.add(clienteFake);

        BDDMockito.given(service.listar(Mockito.any(Cliente.class)))
                .willReturn(ListaClientesFakes);

        //execucao
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(api_cliente)
                .accept(MediaType.APPLICATION_JSON);

        //verificacao
        mvc.perform(request)
                .andExpect(status().isOk());
    }

    public ClienteDTO prepararClienteDTO(){
        return new ClienteDTO("jessica","93229667000");
    }


}
