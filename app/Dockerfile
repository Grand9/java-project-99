FROM gradle:8.5.0-jdk21 AS build

WORKDIR /app

COPY app/build.gradle.kts app/settings.gradle.kts ./
COPY app/gradle ./gradle

RUN gradle build --no-daemon

COPY app/src ./src

RUN gradle build --no-daemon

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
