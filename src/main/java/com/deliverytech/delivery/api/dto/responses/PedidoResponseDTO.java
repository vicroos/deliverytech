package com.deliverytech.delivery.api.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery.api.enums.StatusPedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoResponseDTO {
    private Long id;
    private LocalDateTime dataPedido;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private String enderecoEntrega;
    
    private List<ItemPedidoResponseDTO> itens; 

    private String nomeCliente;
    private String nomeRestaurante;
}