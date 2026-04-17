package com.deliverytech.delivery.api.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery.api.enums.CategoriaRestaurante;
import com.deliverytech.delivery.api.enums.Role;
import com.deliverytech.delivery.api.enums.StatusPedido;
import com.deliverytech.delivery.api.model.Cliente;
import com.deliverytech.delivery.api.model.ItemPedido;
import com.deliverytech.delivery.api.model.Pedido;
import com.deliverytech.delivery.api.model.Produto;
import com.deliverytech.delivery.api.model.Restaurante;
import com.deliverytech.delivery.api.model.Usuario;
import com.deliverytech.delivery.api.repository.ClienteRepository;
import com.deliverytech.delivery.api.repository.ItemPedidoRepository;
import com.deliverytech.delivery.api.repository.PedidoRepository;
import com.deliverytech.delivery.api.repository.ProdutoRepository;
import com.deliverytech.delivery.api.repository.RestauranteRepository;
import com.deliverytech.delivery.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataLoader {

    @Bean
    @Transactional
    CommandLineRunner iniciarDados(
        ClienteRepository clienteRepository,
        ProdutoRepository produtoRepository,
        PedidoRepository pedidoRepository,
        ItemPedidoRepository itemPedidoRepository,
        RestauranteRepository restauranteRepository,
        UsuarioRepository usuarioRepository,
        PasswordEncoder passwordEncoder
    ){
        return args ->{
            if (usuarioRepository.count() > 1) { // Já existe o admin e possivelmente outros
                return;
            }

            System.out.println("=====Inserindo Clientes======");
            
            Usuario u1 = new Usuario();
            u1.setEmail("joao@gmail.com");
            u1.setSenha(passwordEncoder.encode("123456"));
            u1.setRole(Role.CLIENTE);
            u1.setAtivo(true);
            usuarioRepository.save(u1);

            Cliente c1 = new Cliente();
            c1.setNome("João Freitas");
            c1.setEmail("joao@gmail.com");
            c1.setTelefone("119999-8888");
            c1.setEndereco("av 1, 111");
            c1.setAtivo(true);
            c1.setUsuario(u1);

            Usuario u2 = new Usuario();
            u2.setEmail("mariana@gmail.com");
            u2.setSenha(passwordEncoder.encode("123456"));
            u2.setRole(Role.CLIENTE);
            u2.setAtivo(true);
            usuarioRepository.save(u2);

            Cliente c2 = new Cliente();
            c2.setNome("Mariana Freitas");
            c2.setEmail("mariana@gmail.com");
            c2.setTelefone("119999-7777");
            c2.setEndereco("av 2, 222");
            c2.setAtivo(true);
            c2.setUsuario(u2);


            Usuario u3 = new Usuario();
            u3.setEmail("joanna@gmail.com");
            u3.setSenha(passwordEncoder.encode("123456"));
            u3.setRole(Role.CLIENTE);
            u3.setAtivo(true);
            usuarioRepository.save(u3);

            Cliente c3 = new Cliente();
            c3.setNome("Joanna Silva");
            c3.setEmail("joanna@gmail.com");
            c3.setTelefone("119999-7777");
            c3.setEndereco("av 3, 333");
            c3.setAtivo(true);
            c3.setUsuario(u3);

            clienteRepository.saveAll(List.of(c1, c2, c3));

            System.out.println("======Consultando Clientes======");

            System.out.println("> Buscar por email:");
            clienteRepository.findByEmail("joao@gmail.com").ifPresent(System.out::println);

            System.out.println("> Buscar por cliente contendo 'jo':");
            clienteRepository.findByNomeContainingIgnoreCase("jo")
            .forEach(c -> System.out.println(c.getNome()));

            System.out.println("> Verificar se email existe:");
            boolean existe = clienteRepository.existsByEmail("mariana@gmail.com");
            System.out.println("Existe Maria? " + existe);

            Pageable pageable = PageRequest.of(0, 10);
            System.out.println("> Clientes ativos:");
            clienteRepository.findByAtivoTrue(pageable)
                .forEach(c -> System.out.println(c.getNome()));

            System.out.println("=====Inserindo Restaurante ======");

            Usuario ur1 = new Usuario();
            ur1.setEmail("pizzatop@gmail.com");
            ur1.setSenha(passwordEncoder.encode("123456"));
            ur1.setRole(Role.RESTAURANTE);
            ur1.setAtivo(true);
            usuarioRepository.save(ur1);

            Restaurante r1 = new Restaurante();
            r1.setNome("pizza Top");
            r1.setCategoria(CategoriaRestaurante.PIZZARIA);
            r1.setEndereco("Rua um, 111");
            r1.setTelefone("11 9999-1111");
            r1.setAvaliacao(new BigDecimal(4.5));
            r1.setAtivo(true);
            r1.setUsuario(ur1);

            Usuario ur2 = new Usuario();
            ur2.setEmail("burgerhouse@gmail.com");
            ur2.setSenha(passwordEncoder.encode("123456"));
            ur2.setRole(Role.RESTAURANTE);
            ur2.setAtivo(true);
            usuarioRepository.save(ur2);

            Restaurante r2 = new Restaurante();
            r2.setNome("Burger House");
            r2.setCategoria(CategoriaRestaurante.HAMBURGUERIA);
            r2.setEndereco("Rua dois, 222");
            r2.setTelefone("11 9999-2222");
            r2.setAvaliacao(new BigDecimal(4.2));
            r2.setAtivo(true);
            r2.setUsuario(ur2);

            restauranteRepository.save(r1);
            restauranteRepository.save(r2);

            System.out.println("======Consultando Restaurante======");

            System.out.println("> Buscar Restaurante por Categoria:");

            /* restauranteRepository.findByCategoria("Hamburgueria")
            .forEach(c -> System.out.println("Restaurante(Hamburgueria): " + c.getNome()));

            System.out.println("> Restaurantes ativos:");
            restauranteRepository.findByAtivoTrue()
                .forEach(r -> System.out.println(r.getNome())); */

            System.out.println("=====Inserindo Produtos ======");

            Produto p1 = new Produto();
            p1.setNome("Pizza de calabresa");
            p1.setDescricao("Pizza de calabresa com queijo");
            p1.setPreco(new BigDecimal("40.00"));
            p1.setCategoria(CategoriaRestaurante.PIZZARIA.name());
            p1.setDisponivel(true);
            p1.setRestaurante(r1);

            Produto p2 = new Produto();
            p2.setNome("Pizza de Frango");
            p2.setDescricao("Pizza de frango com catupiry");
            p2.setPreco(new BigDecimal("40.00"));
            p2.setCategoria(CategoriaRestaurante.PIZZARIA.name());
            p2.setDisponivel(true);
            p2.setRestaurante(r1);

            Produto p3 = new Produto();
            p3.setNome("Pizza de Quatro Queijo");
            p3.setDescricao("Pizza com quatro tipos de queijos");
            p3.setPreco(new BigDecimal("50.00"));
            p3.setCategoria(CategoriaRestaurante.PIZZARIA.name());
            p3.setDisponivel(true);
            p3.setRestaurante(r1);


            Produto p4 = new Produto();
            p4.setNome("X-Burger");
            p4.setDescricao("Hambúrguer tradicional");
            p4.setPreco(new BigDecimal("25.00"));
            p4.setCategoria(CategoriaRestaurante.HAMBURGUERIA.name());
            p4.setDisponivel(true);
            p4.setRestaurante(r2);

            produtoRepository.saveAll(List.of(p1, p2, p3, p4));

            System.out.println("=====Inserindo Pedidos ======");
            Pedido pedido1 = new Pedido();
            pedido1.setCliente(c1);
            pedido1.setEnderecoEntrega("av 1, 111");
            pedido1.setStatus(StatusPedido.PENDENTE);
            pedido1.setTaxaEntrega(new BigDecimal("5.00"));
            pedido1.setValorTotal(BigDecimal.ZERO);
            pedido1.setRestaurante(r1);

            Pedido pedido2 = new Pedido();
            pedido2.setCliente(c2);
            pedido2.setEnderecoEntrega("av 2, 222");
            pedido2.setStatus(StatusPedido.PENDENTE);
            pedido2.setTaxaEntrega(new BigDecimal("5.00"));
            pedido2.setValorTotal(BigDecimal.ZERO);
            pedido2.setRestaurante(r2);

            pedidoRepository.saveAll(List.of(pedido1, pedido2));

            System.out.println("=====Inserindo ItensPedido ======");
            ItemPedido i1 = new ItemPedido();
            i1.setPedido(pedido1); 
            i1.setProduto(p1);
            i1.setPrecoUnitario(p1.getPreco());
            i1.setQuantidade(2);
            i1.setSubtotal(i1.getPrecoUnitario().multiply(BigDecimal.valueOf(i1.getQuantidade())));


            ItemPedido i2 = new ItemPedido();
            i2.setPedido(pedido2); 
            i2.setProduto(p2);
            i2.setPrecoUnitario(p2.getPreco());
            i2.setQuantidade(1);
            i2.setSubtotal(i2.getPrecoUnitario().multiply(BigDecimal.valueOf(i2.getQuantidade())));

            itemPedidoRepository.saveAll(List.of(i1, i2));

            pedido1.setValorTotal(i1.getSubtotal().add(pedido1.getTaxaEntrega()));
            pedido2.setValorTotal(i2.getSubtotal().add(pedido2.getTaxaEntrega()));
            pedidoRepository.save(pedido1);
            pedidoRepository.save(pedido2);

            System.out.println("=====DTO - Itens do pedido ======");
            itemPedidoRepository.buscarItensPorPedido(pedido1.getId())
            .forEach(i -> System.out.println(
                "Produto: " + i.getNomeProduto() +
                "| Qtd: " + i.getQuantidade() + 
                "| Subtotal: " + i.getSubtotal() 
            ));

            itemPedidoRepository.buscarItensPorPedido(pedido2.getId())
            .forEach(i -> System.out.println(
                "Produto: " + i.getNomeProduto() +
                "| Qtd: " + i.getQuantidade() + 
                "| Subtotal: " + i.getSubtotal() 
            ));

            System.out.println("=====DTO - Vendas por restaurante ======");
            pedidoRepository.findAllWithRestaurante()
            .forEach(p -> {
                String nome = (p.getRestaurante() != null) ? p.getRestaurante().getNome() : "Restaurante Nulo";
                System.out.println(nome + " - " + p.getValorTotal());
            });
        };
    }
}
