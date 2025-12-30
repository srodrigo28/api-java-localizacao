FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

# Copia os arquivos de configuração do Maven/Gradle e baixa as dependências
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

# Copia o código-fonte e compila a aplicação
COPY src src
RUN ./mvnw package -DskipTests

# ---

# Estágio Final: Usa uma imagem JRE leve para rodar a aplicação
FROM eclipse-temurin:21-jre

# Cria um usuário e grupo não-root
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

WORKDIR /app

# Copia o jar do estágio de build para a imagem final
COPY --from=builder /workspace/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]