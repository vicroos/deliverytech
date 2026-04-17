package com.deliverytech.delivery.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deliverytech.delivery.api.enums.Role;
import com.deliverytech.delivery.api.model.Usuario;
import com.deliverytech.delivery.api.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!usuarioRepository.existsByEmail("admin@admin.com")) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@admin.com");
                admin.setSenha(passwordEncoder.encode("123456"));
                admin.setRole(Role.ADMIN);
                usuarioRepository.save(admin);
                System.out.println("=================================================");
                System.out.println("Usuário padrão criado com sucesso!");
                System.out.println("Email: admin@admin.com");
                System.out.println("Senha: 123456");
                System.out.println("=================================================");
            }
        };
    }
}
