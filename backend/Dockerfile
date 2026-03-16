# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Create upload directory and set permissions
RUN mkdir -p /app/uploads && chmod 777 /app/uploads

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
