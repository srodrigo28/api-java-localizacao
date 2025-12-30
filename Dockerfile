FROM eclipse-temurin:21-jdk AS builder

WORKDIR /workspace

# Copia os arquivos de configuração do Maven/Gradle e baixa as dependências
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
