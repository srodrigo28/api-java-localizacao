package com.treinadev.calcularkm;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
// ...existing code...
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
// ...existing code...
import jakarta.validation.Valid;

@RestController
@RequestMapping("/distance")
public class DistanceController {

    private static final Logger log = LoggerFactory.getLogger(DistanceController.class);

    private final DistanceService distanceService;

    /**
     * Lista de Locales suportados pelo serviço.
     * Observação: ajuste conforme os idiomas que deseja atender.
     */
    private static final List<Locale> SUPPORTED_LOCALES = List.of(
            Locale.forLanguageTag("pt-BR"),
            Locale.forLanguageTag("pt-PT"),
            Locale.forLanguageTag("en-US")
    );

    /**
     * Injeção de dependência via construtor.
     * Regra: Controller apenas orquestra a chamada ao service e prepara a resposta.
     */
    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    /**
     * Calcula a distância em quilômetros entre as coordenadas do usuário e o alvo.
     *
     * Melhorias aplicadas:
     * - Validação do corpo via @Valid (usa anotações do DTO).
     * - Parsing de Accept-Language considerando múltiplos idiomas e qualidade (q=...).
     * - Formatação localizada e log claro da operação.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DistanceResponse> calculate(
            @Valid @RequestBody DistanceRequest req,
            @RequestHeader(name = "Accept-Language", required = false) String acceptLanguage) {

        // Calcula a distância em quilômetros (service já arredonda)
        double distanceKm = distanceService.calculateKm(
                req.getUserLat(),
                req.getUserLng(),
                req.getTargetLat(),
                req.getTargetLng()
        );

        // Resolve Locale a partir do cabeçalho Accept-Language com fallback
        Locale locale = resolveLocale(acceptLanguage);

        // Formata a distância para exibição amigável de acordo com o Locale
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(2); // até 2 casas decimais para leitura
        String distanceKmFormatted = nf.format(distanceKm);

        DistanceResponse body = new DistanceResponse(distanceKm, distanceKmFormatted);

        // Log informativo (sem dados sensíveis)
        log.info("Distância calculada: {} km | locale={} | origem=({}, {}) destino=({}, {})",
                distanceKm, locale, req.getUserLat(), req.getUserLng(), req.getTargetLat(), req.getTargetLng());

        return ResponseEntity.ok(body);
    }

    /**
     * Endpoint simples de verificação (health-check).
     * Ex.: GET /distance/ping -> "ok"
     */
    @GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }

    /**
     * Resolve o Locale do cliente a partir do cabeçalho Accept-Language.
     * - Suporta múltiplos idiomas com peso (ex.: "pt-BR,pt;q=0.9,en-US;q=0.8").
     * - Faz lookup contra a lista SUPPORTED_LOCALES e usa Locale.getDefault() como fallback.
     */
    private Locale resolveLocale(String acceptLanguage) {
        if (acceptLanguage == null || acceptLanguage.isBlank()) {
            return Locale.getDefault();
        }
        try {
            var ranges = Locale.LanguageRange.parse(acceptLanguage);
            Locale matched = Locale.lookup(ranges, SUPPORTED_LOCALES);
            return matched != null ? matched : Locale.getDefault();
        } catch (IllegalArgumentException ex) {
            // Cabeçalho malformado: usa fallback padrão
            log.debug("Accept-Language inválido '{}': usando Locale default.", acceptLanguage);
            return Locale.getDefault();
        }
    }
}