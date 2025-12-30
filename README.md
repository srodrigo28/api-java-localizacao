# API de Cálculo de Distância

Uma simples API REST construída com Java e Spring Boot para calcular a distância em quilômetros entre duas coordenadas geográficas (latitude e longitude).

## ✨ Tecnologias

Este projeto foi desenvolvido com as seguintes tecnologias:

*   **Java 21**
*   **Spring Boot 3**
*   **Maven**
*   **Docker**

> * Link frontend
```vercel domain
https://api-frontend-localizacao.vercel.app/
```

> * Link frontend
```github
https://github.com/srodrigo28/api-frontend-localizacao
```

## 🚀 Como Executar

Siga as instruções abaixo para executar o projeto em seu ambiente local.

### Pré-requisitos

*   Java (JDK 21 ou superior)
*   Maven
*   Docker (Opcional, para rodar via contêiner)

### Rodando Localmente

1.  Clone o repositório:
    ```bash
    git clone <url-do-seu-repositorio>
    ```
2.  Navegue até o diretório do projeto:
    ```bash
    cd calcularkm
    ```
3.  Execute a aplicação com o Maven Wrapper:
    ```bash
    ./mvnw spring-boot:run
    ```
4.  A aplicação estará disponível em `http://localhost:8080`.

## ☁️ Deploy

A aplicação está disponível publicamente através do Render.

**URL Base:** `https://api-java-localizacao.onrender.com`

## Endpoints da API

### `POST /distance`

Calcula a distância entre um ponto de origem e um ponto de destino.

#### Request Body

Envie as coordenadas no corpo da requisição:

```json
{
  "userLat": -23.55052,
  "userLng": -46.633308,
  "targetLat": -22.906847,
  "targetLng": -43.172897
}
```

#### Exemplo de Uso com `curl`

```bash
curl -X POST \
  https://api-java-localizacao.onrender.com/distance \
  -H 'Content-Type: application/json' \
  -d '{
        "userLat": -23.55052,
        "userLng": -46.633308,
        "targetLat": -22.906847,
        "targetLng": -43.172897
      }'
```

#### Exemplo de Resposta (Sucesso)

```json
{
  "distanceKm": 357.42
}
```