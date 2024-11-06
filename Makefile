clean:
	./gradlew clean
run-dist:
	./build/install/app-boot/bin/app
build:
	./gradlew clean build
install:
	./gradlew installBootDist
lint:
	./gradlew checkstyleMain
test:
	./gradlew test
report:
	./gradlew jacocoTestReport

.PHONY: build
