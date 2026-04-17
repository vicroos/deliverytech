package com.deliverytech.delivery.api.service;

import java.math.BigDecimal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery.api.dto.requests.ItemPedidoDTO;
import com.deliverytech.delivery.api.dto.requests.PedidoDTO;
import com.deliverytech.delivery.api.dto.responses.PedidoResponseDTO;
import com.deliverytech.delivery.api.enums.StatusPedido;
import com.deliverytech.delivery.api.exception.BusinessException;
import com.deliverytech.delivery.api.exception.EntityNotFoundException;
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

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    private final ModelMapper mapper;

    private PedidoResponseDTO toResponseDTO(Pedido pedido){
        return mapper.map(pedido, PedidoResponseDTO.class);
    }

    public PedidoService(
            PedidoRepository pedidoRepository,
            ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository,
            ItemPedidoRepository ItemPedidoRepository,
            ModelMapper mapper,
            ProdutoRepository produtoRepository) {

        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.restauranteRepository = restauranteRepository;
        this.produtoRepository = produtoRepository;
        this.mapper = mapper;
    }

        private PedidoResponseDTO toDTO(Pedido pedido) {
        return mapper.map(pedido, PedidoResponseDTO.class);
    }

    @Transactional
    public PedidoResponseDTO criarPedido(PedidoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));

        if (!cliente.isAtivo()) throw new BusinessException("Cliente inativo.");

        Restaurante restaurante = restauranteRepository.findById(dto.getRestauranteId())
            .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado."));

        if (!restaurante.isAtivo()) throw new BusinessException("Restaurante inativo.");

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());

        BigDecimal total = BigDecimal.ZERO;

        for (ItemPedidoDTO itemDTO : dto.getItens()) {
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado."));

            if (!produto.isDisponivel()) throw new BusinessException("Produto indisponível: " + produto.getNome());

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(produto.getPreco());
            
            BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            item.setSubtotal(subtotal);
            
            pedido.getItens().add(item);
            total = total.add(subtotal);
        }

        pedido.setValorTotal(total);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

        private void validarDonoPedido(Pedido pedido, Usuario usuarioLogado) {
        if (!pedido.getCliente().getEmail().equals(usuarioLogado.getEmail())) {
            throw new BusinessException("Você não tem permissão para acessar este pedido.");
        }
    }


    @Transactional
    public PedidoResponseDTO confirmarPedido(Long pedidoId){
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new EntityNotFoundException("Pedido não localizado.") );

        if(pedido.getStatus() != StatusPedido.PENDENTE){
            throw new BusinessException("Apenas pedidos PENDENTES podem ser confirmados.");
        }

        pedido.setStatus(StatusPedido.CONFIRMADO);
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long pedidoId){
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(()-> new EntityNotFoundException("Pedido não encontrado."));

        StatusPedido statusAtual = pedido.getStatus();

        switch(statusAtual){
            case CONFIRMADO -> pedido.setStatus(StatusPedido.PREPARANDO);
            case PREPARANDO -> pedido.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
            case SAIU_PARA_ENTREGA -> pedido.setStatus(StatusPedido.ENTREGUE);

            case CANCELADO, ENTREGUE -> 
                throw new BusinessException("Status do Pedido não pode mais ser avançado.");
            default ->
                throw new BusinessException("Status é inválido para avanço.");
        }
        return toResponseDTO(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarPorCliente(Long clienteId, Pageable pageable) {
        return pedidoRepository.buscarItensPorClientes(clienteId, pageable)
            .map(this::toResponseDTO);
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(Long pedidoId, Usuario usuarioLogado){
        Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(()-> new EntityNotFoundException("Pedido não encontrado."));

        validarDonoPedido(pedido, usuarioLogado);

        if(pedido.getStatus() == StatusPedido.ENTREGUE){
            throw new BusinessException("Pedido entregue não pode ser cancelado.");
        }

        pedido.setStatus(StatusPedido.CANCELANDO);
        Pedido salvo = pedidoRepository.save(pedido);
        return toResponseDTO(salvo);
    }

    public Page<PedidoResponseDTO> meusPedidos(Usuario usuarioLogado, Pageable pageable) {

        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        return pedidoRepository.buscarItensPorClientes(cliente.getId(), pageable)
                .map(this::toDTO);
    }
}