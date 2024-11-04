FROM openjdk:20-jdk-slim AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY gradle.properties ./
RUN ./gradlew dependencies
COPY . .
RUN ./gradlew build --no-daemon
FROM openjdk:20-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
