Vou montar um exemplo COMPLETO e funcional de Next.js (front) + API Java (back) para:

✅ pegar a localização do usuário
✅ enviar para a API Java
✅ calcular a distância em km
✅ retornar o valor para o front

Vou usar a forma mais simples, gratuita e profissional:

Geolocation do navegador

Java (Spring Boot)

Fórmula de Haversine

🧱 Arquitetura do exemplo
[ Browser ]
   ↓ (latitude, longitude)
[ Next.js ]
   ↓ HTTP POST
[ API Java (Spring Boot) ]
   ↓
[ Distância em KM ]

1️⃣ BACK-END — API Java (Spring Boot)
📦 Dependências (Spring Initializr)

Marque:

Spring Web

Lombok (opcional)

📁 Estrutura
src
 └── main
     └── java
         └── com.example.distance
             ├── DistanceController.java
             ├── DistanceService.java
             └── DistanceRequest.java

📄 DistanceRequest.java
public class DistanceRequest {
    public double userLat;
    public double userLng;
    public double targetLat;
    public double targetLng;
}

📄 DistanceService.java
public class DistanceService {

    public static double calculateKm(
            double lat1, double lon1,
            double lat2, double lon2) {

        final int R = 6371; // raio da Terra em km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
}

📄 DistanceController.java
@RestController
@RequestMapping("/distance")
@CrossOrigin // libera chamadas do Next.js
public class DistanceController {

    @PostMapping
    public Map<String, Object> calculate(@RequestBody DistanceRequest req) {

        double distanceKm = DistanceService.calculateKm(
                req.userLat,
                req.userLng,
                req.targetLat,
                req.targetLng
        );

        Map<String, Object> response = new HashMap<>();
        response.put("distanceKm", Math.round(distanceKm * 100.0) / 100.0);

        return response;
    }
}

▶️ Teste rápido (Postman / Insomnia)

POST

http://localhost:8080/distance


Body

{
  "userLat": -23.55052,
  "userLng": -46.633308,
  "targetLat": -22.906847,
  "targetLng": -43.172897
}
