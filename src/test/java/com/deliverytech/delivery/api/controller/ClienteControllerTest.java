package com.deliverytech.delivery.api.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;

import com.deliverytech.delivery.api.dto.requests.ClienteDTO;
import com.deliverytech.delivery.api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery.api.exception.BusinessException;
import com.deliverytech.delivery.api.service.ClienteService;
import com.deliverytech.delivery.api.exception.GlobalExceptionHandler;
import com.deliverytech.delivery.api.model.Cliente;
import com.deliverytech.delivery.api.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.deliverytech.delivery.api.exception.EntityNotFoundException;

@WebMvcTest(controllers = { ClienteController.class,
        GlobalExceptionHandler.class }, excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService service;

    private ClienteDTO clienteValido() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("João Silva");
        dto.setEmail("joao@gmail.com");
        dto.setTelefone("(11)9999-9999");
        dto.setEndereco("Rua A, 123");

        return dto;
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    @DisplayName("Deve cadastrar um cliente com sucesso")
    void shouldCadastrarCliente() throws Exception {
        ClienteDTO dto = clienteValido();
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setNome("João Silva");

        when(service.cadastrar(any(), any())).thenReturn(response);

        mockMvc.perform(post("/api/clientes/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve retornar 400 quando o DTO for inválido")
    void shouldReturn400WhenDTOIsInvalidSemCamposObrigatorios() throws Exception {
        ClienteDTO dto = new ClienteDTO();
        mockMvc.perform(post("/api/clientes/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    @DisplayName("Deve retornar erro ao tentar cadastrar cliente sem autenticação")
    void shouldReturnErroAoCadastrarClienteWithoutAuthentication() throws Exception {
        ClienteDTO dto = clienteValido();
        when(service.cadastrar(any(), any()))
                .thenThrow(new BusinessException("Cliente já cadastrado"));

        mockMvc.perform(post("/api/clientes/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "teste@gmail.com")
    void shouldListarClientesAtivos() throws Exception {
        ClienteResponseDTO cliente = new ClienteResponseDTO();
        cliente.setNome("Victor Silva");

        List<ClienteResponseDTO> lista = List.of(cliente);

        Page<ClienteResponseDTO> pageResponse = new PageImpl<>(lista);

        when(service.listarAtivos(any())).thenReturn(pageResponse);

        mockMvc.perform(get("/api/clientes")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nome").value("Victor Silva"));
    }

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void shouldBuscarClientePorId() throws Exception {
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setNome("João Pedro");

        when(service.buscarPorId(anyLong())).thenReturn(response);

        mockMvc.perform(get("/api/clientes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nome").value("João Pedro"))
                .andDo(print());

    }

    @Test
    void shouldRetornar404AoBuscarClienteInexistente() throws Exception {
        when(service.buscarPorId(anyLong()))
                .thenThrow(new EntityNotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/api/clientes/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldInativarCliente() throws Exception {
        ClienteResponseDTO response = new ClienteResponseDTO();
        response.setId(1L);

        when(service.inativar(anyLong())).thenReturn(response);

        mockMvc.perform(put("/api/clientes/{id}/inativar-cliente", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andDo(print());
    }

}