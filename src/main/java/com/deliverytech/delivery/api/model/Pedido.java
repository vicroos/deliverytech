package com.deliverytech.delivery.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery.api.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="pedidos")
@Schema(description = "Entidade que representa um Pedido")
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do pedido", example = "1001")
    private Long id;

    @Schema(description = "Data e hora em que o pedido foi realizado")
    @Column(name="data_pedido")
    private LocalDateTime dataPedido;

    @Schema(description = "Endereço de entrega do pedido", example = "Av. Paulista, 1000")
    @Column(name="endereco_entrega")
    private String enderecoEntrega;


    @Schema(description = "Taxa de entrega aplicada", example = "7.50")
    @Column(name="taxa_entrega")
    private BigDecimal taxaEntrega;

    @Schema(description = "Valor total do pedido (itens + taxa)", example = "105.40")
    @Column(name="valor_total")
    private BigDecimal valorTotal;

    @Schema(description = "Status atual do pedido", example = "PENDENTE")
    @Enumerated(EnumType.STRING)
    private StatusPedido status;


    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="cliente_id")
    private Cliente cliente;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="restaurante_id")
    private Restaurante restaurante;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemPedido> itens = new ArrayList<>();


    @PrePersist
    public void prePersist(){
        this.dataPedido = LocalDateTime.now();
    }
}
