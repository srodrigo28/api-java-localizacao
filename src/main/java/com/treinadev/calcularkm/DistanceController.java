package com.treinadev.calcularkm;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/distance")
public class DistanceController {
    
    private final DistanceService distanceService;
    
    // Injeção de dependência via construtor
    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }
    
    @PostMapping("")
    public Map<String, Object> calculate(@RequestBody DistanceRequest req) {
        double distanceKm = distanceService.calculateKm(
                req.userLat,
                req.userLng,
                req.targetLat,
                req.targetLng
        );
        Map<String, Object> response = new HashMap<>();
        response.put("distanceKm", distanceKm); // Já está arredondado no service
        return response;
    }    

    @GetMapping("")
    public String teste(){
        return "teste";
    }
}