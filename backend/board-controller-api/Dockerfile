# Etapa de build
FROM maven:3.8.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia os arquivos de configuração do Maven para cache eficiente
COPY pom.xml .  
RUN mvn dependency:go-offline

# Copia o código-fonte
COPY src ./src

# Compila o projeto
RUN mvn clean package -DskipTests

# Segunda etapa: imagem final
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copia o JAR gerado na etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Define a porta corretamente com a variável do Cloud Run
ENV PORT=8080
EXPOSE 8080

# Comando de inicialização com a porta dinâmica do Cloud Run
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=${PORT}"]

