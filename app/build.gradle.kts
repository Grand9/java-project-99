plugins {
	java
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("jacoco")
	checkstyle
	application
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass.set("hexlet.code.AppApplication")
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.4.0")
	implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")
	implementation("io.jsonwebtoken:jjwt:0.9.1")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
	implementation("org.springframework:spring-core:6.1.13")
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.mockito:mockito-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.mockito:mockito-junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
}



tasks.test {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}