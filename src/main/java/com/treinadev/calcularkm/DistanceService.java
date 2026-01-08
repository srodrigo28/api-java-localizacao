package com.treinadev.calcularkm;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// ...existing code...
@Service
public class DistanceService {

    private static final Logger logger = LoggerFactory.getLogger(DistanceService.class);

    // Raio médio da Terra (WGS84) em quilômetros — maior precisão que 6371.
    private static final double EARTH_RADIUS_KM = 6371.0088;

    // Casas decimais para arredondamento do resultado.
    private static final int DECIMAL_PLACES = 2;

    /**
     * Calcula a distância entre duas coordenadas geográficas usando a fórmula de Haversine.
     *
     * Melhorias:
     * - Usa raio WGS84 para maior precisão.
     * - Arredondamento determinístico com BigDecimal (HALF_UP).
     * - Curto-circuito para coordenadas idênticas (retorna 0).
     * - Clampeia 'a' em [0, 1] para evitar erros numéricos em valores extremos.
     *
     * @param userLat Latitude do ponto de origem (usuário) em graus decimais (-90 a 90)
     * @param userLon Longitude do ponto de origem (usuário) em graus decimais (-180 a 180)
     * @param targetLat Latitude do ponto de destino em graus decimais (-90 a 90)
     * @param targetLon Longitude do ponto de destino em graus decimais (-180 a 180)
     * @return Distância em quilômetros, arredondada para 2 casas decimais
     * @throws IllegalArgumentException se as coordenadas estiverem fora dos limites válidos
     */
    public double calculateKm(
            double userLat, double userLon,
            double targetLat, double targetLon) {

        // Valida as coordenadas de entrada (defesa extra além de @Valid no DTO)
        validateCoordinate(userLat, userLon);
        validateCoordinate(targetLat, targetLon);

        // Se as coordenadas forem idênticas, a distância é zero
        if (Double.compare(userLat, targetLat) == 0 && Double.compare(userLon, targetLon) == 0) {
            return 0.0;
        }

        logger.debug("Calculando distância: Origem({}, {}) -> Destino({}, {})",
                userLat, userLon, targetLat, targetLon);

        // Converte graus para radianos
        double lat1Rad = Math.toRadians(userLat);
        double lat2Rad = Math.toRadians(targetLat);
        double dLat = lat2Rad - lat1Rad;
        double dLon = Math.toRadians(targetLon - userLon);

        // Fórmula de Haversine
        double sinLat = Math.sin(dLat / 2.0);
        double sinLon = Math.sin(dLon / 2.0);

        double a = sinLat * sinLat
                + Math.cos(lat1Rad) * Math.cos(lat2Rad) * sinLon * sinLon;

        // Proteção contra erros numéricos (às vezes 'a' pode sair ligeiramente > 1.0)
        a = Math.max(0.0, Math.min(1.0, a));

        // Usando asin para melhor estabilidade quando 'a' está muito próximo de 1
        double c = 2.0 * Math.asin(Math.sqrt(a));

        double distance = EARTH_RADIUS_KM * c;

        // Arredonda para as casas decimais definidas usando BigDecimal (HALF_UP)
        double rounded = roundToDecimals(distance, DECIMAL_PLACES);

        logger.debug("Distância calculada: {} km (precisa antes do arredondamento: {})", rounded, distance);

        return rounded;
    }

    /**
     * Valida se as coordenadas estão dentro dos limites válidos.
     *
     * @param lat Latitude em graus decimais
     * @param lon Longitude em graus decimais
     * @throws IllegalArgumentException se as coordenadas estiverem fora dos limites
     */
    private void validateCoordinate(double lat, double lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException(
                    String.format("Latitude inválida: %.6f. Deve estar entre -90 e 90.", lat)
            );
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException(
                    String.format("Longitude inválida: %.6f. Deve estar entre -180 e 180.", lon)
            );
        }
    }

    /**
     * Arredonda um valor de forma determinística para N casas decimais.
     * Preferível a Math.round para evitar surpresas de representação binária do double.
     */
    private double roundToDecimals(double value, int decimals) {
        return BigDecimal.valueOf(value)
                .setScale(decimals, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
// ...existing code...