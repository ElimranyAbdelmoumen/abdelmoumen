# Build stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN apk add --no-cache maven && mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests -B && mv target/*.jar app.jar

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -g 1000 appgroup && adduser -u 1000 -G appgroup -D appuser
WORKDIR /app

COPY --from=build /app/app.jar app.jar
RUN chown -R appuser:appgroup /app

USER appuser
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
