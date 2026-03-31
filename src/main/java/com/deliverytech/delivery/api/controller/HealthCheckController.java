package com.deliverytech.delivery.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;

@RestController
@Tag(name = "Health Check", description = "Verifica o status da API")
public class HealthCheckController {

    @Operation(summary = "Verifica se a API está no ar")
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "servico", "Delivery API"
        );
    }
}