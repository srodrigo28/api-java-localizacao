# Estágio 1: Build da aplicação
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Copia os arquivos do Maven e baixa as dependências
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copia o código-fonte e compila o projeto
COPY src ./src
RUN ./mvnw package -DskipTests

# ---

# Estágio 2: Imagem final de execução
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o JAR compilado do estágio de build
COPY --from=builder /workspace/target/*.jar app.jar

# Expõe a porta e define o comando para iniciar a aplicação
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT:8080}"]