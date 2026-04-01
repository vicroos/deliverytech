package com.deliverytech.delivery.api.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.deliverytech.delivery.api.model.Cliente;
import com.deliverytech.delivery.api.repository.ClienteRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner iniciarDados(ClienteRepository clienteRepository) {
        return args -> {
            System.out.println("=====Inserindo Clientes=====");

            Cliente c1 = new Cliente();
            c1.setNome("João Freitas");
            c1.setEmail("João@gmail.com");
            c1.setTelefone("(11) 99999-8888");
            c1.setEndereco("Rua das Flores, 123");
            c1.setAtivo(true);

            Cliente c2 = new Cliente();
            c2.setNome("Maria Silva");
            c2.setEmail("Maria@gmail.com");
            c2.setTelefone("(11) 99999-7777");
            c2.setEndereco("Avenida Paulista, 456");
            c2.setAtivo(true);

            Cliente c3 = new Cliente();
            c3.setNome("Joanna Silva");
            c3.setEmail("joanna@gmail.com");
            c3.setTelefone("(11) 9999-6666");
            c3.setEndereco("av 3, 333");
            c3.setAtivo(true);

            clienteRepository.saveAll(List.of(c1, c2, c3));

        };
    }
}
