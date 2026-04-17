package com.deliverytech.delivery.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery.api.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository UsuarioRepositoy){
        this.usuarioRepository = UsuarioRepositoy;
    }
    
    public UserDetails loadUserByUsername(String email)
    throws UsernameNotFoundException {

        return usuarioRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
    }

	}

