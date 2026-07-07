FROM maven:3.9.8-eclipse-temurin-25 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 7071
ENTRYPOINT ["java", "-jar", "app.jar", "--server.port=7071"]