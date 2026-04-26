package com.deliverytech.delivery.api.service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery.api.dto.requests.ClienteDTO;
import com.deliverytech.delivery.api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery.api.enums.Role;
import com.deliverytech.delivery.api.exception.BusinessException;
import com.deliverytech.delivery.api.model.Usuario;
import com.deliverytech.delivery.api.repository.ClienteRepository;
import com.deliverytech.delivery.api.model.Cliente;
import com.deliverytech.delivery.api.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceIT {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private ClienteService service;

    private ClienteDTO dto;
    private Usuario usuario;
    private Cliente cliente;
    private ClienteResponseDTO clienteResponseDTO;

    @BeforeEach
    void setUp() {
        dto = new ClienteDTO("Beatriz Silva", "bia@gmail.com", "119999-9999", "Rua Teste, 123");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("bia@gmail.com");
        usuario.setRole(Role.CLIENTE);
        usuario.setAtivo(true);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setUsuario(usuario);
        cliente.setEmail(dto.getEmail());
        cliente.setAtivo(true);

        clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setId(1L);
        clienteResponseDTO.setEmail("bia@gmail.com");
    }

    @Test
    void shouldCadastrarClienteComSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(clienteRepository.existsByUsuarioId(1L)).thenReturn(false);
        when(clienteRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(mapper.map(dto, Cliente.class)).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponseDTO.class)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = service.cadastrar(dto, usuario);

        assertNotNull(resultado);
        assertEquals("bia@gmail.com", resultado.getEmail());
        verify(clienteRepository).save(any());
    }

    @Test
    void shouldLancarExcecaoQuandoClienteJaExistir() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(clienteRepository.existsByUsuarioId(1L)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class, () -> {
            service.cadastrar(dto, usuario);
        });

        assertEquals("Usuário já possui um perfil de cliente.", ex.getMessage());
    }

    @Test
    void shouldListarClientesAtivos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Cliente> page = new PageImpl<>(List.of(cliente));

        when(clienteRepository.findByAtivoTrue(pageable)).thenReturn(page);
        when(mapper.map(any(Cliente.class), eq(ClienteResponseDTO.class))).thenReturn(clienteResponseDTO);

        Page<ClienteResponseDTO> resultado = service.listarAtivos(pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        verify(mapper, atLeastOnce()).map(any(Cliente.class), eq(ClienteResponseDTO.class));
    }

    @Test
    void shouldBuscarClientePorIdComSucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(mapper.map(cliente, ClienteResponseDTO.class)).thenReturn(clienteResponseDTO);

        ClienteResponseDTO resultado = service.buscarPorId(1L);
        assertNotNull(resultado);
    }

    @Test
    void shouldLancarExcecaoQuandoClienteNaoEncontrado() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            service.buscarPorId(1L);
        });
    }

    @Test
    void shouldInativarOuAtivarCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);
        when(mapper.map(cliente, ClienteResponseDTO.class)).thenReturn(clienteResponseDTO);
        ClienteResponseDTO resultado = service.inativar(1L);

        assertNotNull(resultado);
        verify(clienteRepository).save(any());
    }

    @Test
    void shouldLancarExcecaoQuandoIdNaoEncontradoEmInativar(){
        when(clienteRepository.findById(1l)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> {
            service.inativar(1L);
        });
    }

}

