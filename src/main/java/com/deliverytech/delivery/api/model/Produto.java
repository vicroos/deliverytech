package com.deliverytech.delivery.api.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "produtos")
@Schema(description = "Entidade que representa um Produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Molho de tomate, mozarela, manjericão fresco e azeite.")
    private String descricao;

    @Schema(description = "Preço unitário", example = "45.90")
    private BigDecimal preco;

    @Schema(description = "Categoria do produto", example = "PIZZAS")
    private String categoria;

    @Schema(description = "Indica se o produto está disponível no cardápio", example = "true")
    private boolean disponivel;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    private Restaurante restaurante;
}