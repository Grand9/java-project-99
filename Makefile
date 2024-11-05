DEFAULT_GOAL := build-run

setup:
	gradle wrapper --gradle-version 8.7

clean:
	./gradlew clean

build:
	./gradlew build

install:
	./gradlew install

run-dist:
	./build/install/app/bin/app

run:
	./gradlew run

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain

check-deps:
	./gradlew dependencyUpdates -Drevision=release

build-run: build run

.PHONY: setup clean build install run-dist run test report lint check-deps build-run
