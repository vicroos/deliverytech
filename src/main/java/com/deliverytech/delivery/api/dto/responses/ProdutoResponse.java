package com.deliverytech.delivery.api.dto.responses;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "Dados de retorno de um produto.")
public class ProdutoResponse {

    @Schema(description = "ID único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Pizza Margherita")
    private String nome;

    @Schema(description = "Descrição detalhada", example = "Molho de tomate e mozarela.")
    private String descricao;

    @Schema(description = "Categoria do produto", example = "PIZZAS")
    private String categoria;

    @Schema(description = "Preço unitário", example = "45.00")
    private BigDecimal preco;

    @Schema(description = "Disponibilidade em estoque", example = "true")
    private boolean disponivel;

    @Schema(description = "ID do restaurante vinculado", example = "10")
    private Long restauranteId;
}
