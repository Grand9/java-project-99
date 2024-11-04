FROM eclipse-temurin:21-jdk AS build
WORKDIR /project
COPY gradlew ./
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY gradle/ gradle/
COPY src/ src/
RUN chmod +x gradlew
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /project
COPY --from=build /project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
