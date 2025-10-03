import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    kotlin("plugin.jpa") version "2.0.21"
}

group = "pl.edu.agh.timeslotmachine.backend"
version = "0.0.1-SNAPSHOT"

val mainBackendClass = "$group.BackendApplicationKt"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
//    implementation ("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
//    implementation ("org.springframework.security:spring-security-oauth2-jose")
    // https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt
    implementation("io.jsonwebtoken:jjwt:0.12.6")

    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui
    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

    // Kotlin Logging
    implementation("io.github.oshai:kotlin-logging-jvm:6.0.1")

    // https://mvnrepository.com/artifact/com.google.ortools/ortools-java
    // Google OR-Tools
    implementation("com.google.ortools:ortools-java:9.8.3296")

    // https://mvnrepository.com/artifact/org.mnode.ical4j/ical4j
    // iCal4j
    implementation("org.mnode.ical4j:ical4j:4.0.5")
}

springBoot {
    mainClass = mainBackendClass
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
