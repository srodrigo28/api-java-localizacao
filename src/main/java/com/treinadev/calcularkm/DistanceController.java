package com.treinadev.calcularkm;

// ...existing code...
import java.util.List;
import java.util.Locale;
// ...existing code...
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

    private static final List<Locale> SUPPORTED_LOCALES = List.of(
            Locale.forLanguageTag("pt-BR"),
            Locale.forLanguageTag("pt-PT"),
            Locale.forLanguageTag("en-US")
    );

    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DistanceResponse> calculate(
            @Valid @RequestBody DistanceRequest req,
            @RequestHeader(name = "Accept-Language", required = false) String acceptLanguage) {

        double distanceKm = distanceService.calculateKm(
                req.getUserLat(),
                req.getUserLng(),
                req.getTargetLat(),
                req.getTargetLng()
        );

        Locale locale = resolveLocale(acceptLanguage);

        // Formata a distância usando utilitário centralizado (2 casas decimais, conforme Locale)
        String distanceKmFormatted = FormatadorLocalizacao.formatarNumero(distanceKm, locale);

        DistanceResponse body = new DistanceResponse(distanceKm, distanceKmFormatted);

        log.info("Distância calculada: {} km | locale={} | origem=({}, {}) destino=({}, {})",
                distanceKm, locale, req.getUserLat(), req.getUserLng(), req.getTargetLat(), req.getTargetLng());

        return ResponseEntity.ok(body);
    }

    @GetMapping(path = "/ping", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("ok");
    }

    private Locale resolveLocale(String acceptLanguage) {
        if (acceptLanguage == null || acceptLanguage.isBlank()) {
            return Locale.getDefault();
        }
        try {
            var ranges = Locale.LanguageRange.parse(acceptLanguage);
            Locale matched = Locale.lookup(ranges, SUPPORTED_LOCALES);
            return matched != null ? matched : Locale.getDefault();
        } catch (IllegalArgumentException ex) {
            log.debug("Accept-Language inválido '{}': usando Locale default.", acceptLanguage);
            return Locale.getDefault();
        }
    }
}