package com.deliverytech.delivery.api.dto.requests;

import java.math.BigDecimal;

import com.deliverytech.delivery.api.validation.CategoriaValida;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Dados para criação ou atualização de um restaurante.")
public class RestauranteRequest {

    @Schema(description = "Nome do restaurante", example = "Pizzaria Bella Napoli")
    @NotBlank(message = "Nome do restaurante é obrigatório")
    private String nome;

    @Schema(description = "Categoria do restaurante", example = "ITALIANA")
    @NotBlank(message = "Categoria é obrigatória")
    @CategoriaValida
    private String categoria;

    @Schema(description = "Endereço completo", example = "Rua das Flores, 123")
    @Size(min = 5, max = 255, message = "Endereço deve ter entre 5 e 255 caracteres")
    private String endereco;

    @Schema(description = "Telefone de contato", example = "(11) 98765-4321")
    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
        regexp = "\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}",
        message = "Telefone inválido. Formato esperado: (XX) XXXXX-XXXX ou similar"
    )
    private String telefone;

    @Schema(description = "Taxa de entrega", example = "5.50")
    @NotNull(message = "A taxa de entrega é obrigatória")
    private BigDecimal taxaEntrega;
}
