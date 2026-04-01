package com.deliverytech.delivery.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.model.Cliente;
import com.deliverytech.delivery.api.repository.ClienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class ClienteService {

    private final ClienteRepository repository;


    public Cliente cadastrar(Cliente cliente){
        if( repository.existsByEmail(cliente.getEmail()) ){
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }
        cliente.setAtivo(true);
        return repository.save(cliente);
    }

    public List<Cliente> listarAtivos(){
        return repository.findByAtivoTrue();
    }

    public Cliente buscarPorId(Long id){
        return repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Cliente não encontrado."));
    }


    public void inativar(Long id){
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        repository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dados){
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());
        cliente.setEndereco(dados.getEndereco());
        return repository.save(cliente);
    }
}