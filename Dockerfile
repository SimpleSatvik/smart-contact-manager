FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Replace 'your-app.jar' with your actual jar name below:
COPY --from=build /app/target/smarContactManager-0.0.1-SNAPSHOT.jar smarContactManager-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
