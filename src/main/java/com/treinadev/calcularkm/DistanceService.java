package com.treinadev.calcularkm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DistanceService {

    private static final Logger logger = LoggerFactory.getLogger(DistanceService.class);
    private static final int EARTH_RADIUS_KM = 6371; // Raio da Terra em quilômetros

    /**
     * Calcula a distância entre duas coordenadas geográficas usando a fórmula de Haversine.
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

        // Valida as coordenadas de entrada
        validateCoordinate(userLat, userLon);
        validateCoordinate(targetLat, targetLon);

        logger.debug("Calculando distância: Origem({}, {}) -> Destino({}, {})", 
            userLat, userLon, targetLat, targetLon);

        // Converte graus para radianos
        double dLat = Math.toRadians(targetLat - userLat);
        double dLon = Math.toRadians(targetLon - userLon);

        // Aplica a fórmula de Haversine
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(userLat))
                * Math.cos(Math.toRadians(targetLat))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;

        // Arredonda para 2 casas decimais
        distance = Math.round(distance * 100.0) / 100.0;

        logger.debug("Distância calculada: {} km", distance);

        return distance;
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
}