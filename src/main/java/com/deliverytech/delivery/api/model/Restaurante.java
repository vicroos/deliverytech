package com.deliverytech.delivery.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.deliverytech.delivery.api.enums.CategoriaRestaurante;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurante")
@Schema(description = "Entidade que representa um Restaurante")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do restaurante", example = "1")
    private Long id;

    @Schema(description = "Nome do restaurante", example = "Pizzaria Bella Napoli")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "ITALIANA")
    private CategoriaRestaurante categoria;

    @Schema(description = "Endereço completo", example = "Rua das Flores, 123")
    private String endereco;

    @Schema(description = "Telefone de contato", example = "(11) 98765-4321")
    private String telefone;

    /*     @Column(name = "taxa_entrega")
    private BigDecimal taxaEntrega; */

    @Schema(description = "Avaliação média (0.0 a 5.0)", example = "4.5")
    private BigDecimal avaliacao;

    @Schema(description = "Status de ativação do restaurante", example = "true")
    private boolean ativo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy= "restaurante", fetch=FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();

    @OneToMany(mappedBy= "restaurante", fetch=FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
}