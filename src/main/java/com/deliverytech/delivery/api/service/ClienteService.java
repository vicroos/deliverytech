package com.deliverytech.delivery.api.service;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.dto.requests.ClienteDTO;
import com.deliverytech.delivery.api.dto.responses.ClienteResponseDTO;
import com.deliverytech.delivery.api.enums.Role;
import com.deliverytech.delivery.api.exception.BusinessException;
import com.deliverytech.delivery.api.exception.EntityNotFoundException;
import com.deliverytech.delivery.api.model.Cliente;
import com.deliverytech.delivery.api.model.Usuario;
import com.deliverytech.delivery.api.repository.ClienteRepository;
import com.deliverytech.delivery.api.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {


    private final ClienteRepository repository;
    private final UsuarioRepository usuarioRepository;

    private final ModelMapper mapper;

    public ClienteService (ClienteRepository repository, ModelMapper mapper, UsuarioRepository usuarioRepository){
        this.repository = repository;
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public ClienteResponseDTO cadastrar(ClienteDTO dto, Usuario usuarioLogado){
        if(usuarioLogado == null){
            throw new BusinessException("Usuário não autenticado.");
        }

        Usuario usuario = usuarioRepository.findById(usuarioLogado.getId())
        .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado no banco de dados."));

        if(usuarioLogado.getRole() != Role.CLIENTE && usuarioLogado.getRole() != Role.ADMIN){
            throw new BusinessException("Apenas CLIENTE ou ADMIN podem cadastrar um perfil de cliente.");
        }
        if(repository.existsByUsuarioId(usuarioLogado.getId())){
            throw new BusinessException("Usuário já possui um perfil de cliente.");

        }
        if( repository.existsByEmail(dto.getEmail()) ){
            throw new BusinessException("E-mail já cadastrado.");
        }
        Cliente cliente = mapper.map(dto, Cliente.class);
        cliente.setAtivo(true);
        cliente.setUsuario(usuarioLogado); 
        Cliente salvo = repository.save(cliente);

        return mapper.map(salvo, ClienteResponseDTO.class);
    }

    public Page<ClienteResponseDTO> listarAtivos(Pageable pageable){
        return repository.findByAtivoTrue(pageable)
        .map(clientes -> mapper.map(clientes, ClienteResponseDTO.class));
    }
    

    public ClienteResponseDTO buscarPorId(Long id){
        Cliente cliente =  repository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado."));

        return mapper.map(cliente, ClienteResponseDTO.class);
    }


    public ClienteResponseDTO inativar(Long id){
        Cliente cliente =  repository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado."));
        cliente.setAtivo(!cliente.isAtivo());
        Cliente salvo = repository.save(cliente);
        return mapper.map(salvo, ClienteResponseDTO.class);
    }

    /* public Cliente atualizar(Long id, Cliente dados){
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());
        cliente.setEndereco(dados.getEndereco());
        return repository.save(cliente);
    } */


    
}