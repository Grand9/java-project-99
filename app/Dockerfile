FROM gradle:8.5-jdk21 AS build

WORKDIR /app
COPY . .

RUN gradle installDist

FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY --from=build /app/build/install/app /app

COPY src/main/resources/certs /app/resources/certs

RUN chmod +x /app/bin/app

CMD ["./bin/app"]
