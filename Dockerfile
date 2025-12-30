# Imagem Java leve e moderna
FROM eclipse-temurin:21-jre

# Pasta de trabalho
WORKDIR /app

# Copia o jar para dentro do container
COPY target/*.jar app.jar

# Porta usada pelo Spring Boot
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
