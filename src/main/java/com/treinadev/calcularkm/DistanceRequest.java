package com.treinadev.calcularkm;

import lombok.Data;
// Caso esteja usando Spring Boot 3+, use jakarta.validation.*.
// Se for Spring Boot 2.x, troque para javax.validation.*.
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO de entrada para cálculo de distância.
 * Regra de validação:
 * - Latitude: deve estar entre -90 e 90.
 * - Longitude: deve estar entre -180 e 180.
 *
 * Observação:
 * - Campos privados para melhor encapsulamento.
 * - @Data gera getters/setters, equals/hashCode e toString automaticamente (via Lombok).
 */
@Data
public class DistanceRequest {

    /**
     * Latitude do usuário em graus decimais (-90 a 90).
     */
    @DecimalMin(value = "-90", inclusive = true, message = "Latitude do usuário deve ser >= -90")
    @DecimalMax(value = "90", inclusive = true, message = "Latitude do usuário deve ser <= 90")
    private double userLat;

    /**
     * Longitude do usuário em graus decimais (-180 a 180).
     */
    @DecimalMin(value = "-180", inclusive = true, message = "Longitude do usuário deve ser >= -180")
    @DecimalMax(value = "180", inclusive = true, message = "Longitude do usuário deve ser <= 180")
    private double userLng;

    /**
     * Latitude do alvo em graus decimais (-90 a 90).
     */
    @DecimalMin(value = "-90", inclusive = true, message = "Latitude do alvo deve ser >= -90")
    @DecimalMax(value = "90", inclusive = true, message = "Latitude do alvo deve ser <= 90")
    private double targetLat;

    /**
     * Longitude do alvo em graus decimais (-180 a 180).
     */
    @DecimalMin(value = "-180", inclusive = true, message = "Longitude do alvo deve ser >= -180")
    @DecimalMax(value = "180", inclusive = true, message = "Longitude do alvo deve ser <= 180")
    private double targetLng;
}