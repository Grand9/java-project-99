plugins {
	application
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("io.freefair.lombok") version "8.6"
	id ("io.sentry.jvm.gradle") version "4.13.0"
	checkstyle
	jacoco
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass.set("hexlet.code.AppApplication")
}

repositories {
	mavenCentral()
}

dependencies {
	runtimeOnly("com.h2database:h2")
	implementation("org.postgresql:postgresql:42.7.3")
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.6.0")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-api:2.6.0")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.6.0.Beta2")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	implementation("net.datafaker:datafaker:2.0.2")
	implementation("org.instancio:instancio-junit:3.3.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
}

tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
	}
}

tasks.test {
	useJUnitPlatform()
}

sentry {
	includeSourceContext = true
	org = "grand-yf"
	projectName = "java-spring-boot"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

tasks.sentryBundleSourcesJava {
	enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}