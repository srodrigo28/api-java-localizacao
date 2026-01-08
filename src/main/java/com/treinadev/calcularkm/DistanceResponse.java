package com.treinadev.calcularkm;

/**
 * DTO de resposta do cálculo de distância.
 * Uso de 'record' (Java 21) para imutabilidade e concisão.
 * Campos:
 * - distanceKm: valor numérico bruto (para consumo por clientes/máquinas).
 * - distanceKmFormatted: valor já formatado conforme Locale (para exibição ao usuário).
 *
 * Observação:
 * - Jackson (usado pelo Spring Boot) serializa records automaticamente.
 */
public record DistanceResponse(double distanceKm, String distanceKmFormatted) { }