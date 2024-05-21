import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
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
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
//	DocuSign
    implementation("com.docusign:docusign-esign-java:4.3.0")
    implementation("javax.ws.rs:javax.ws.rs-api:2.1")
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:3.1.0")
    implementation("org.glassfish.jersey.core:jersey-client:3.0.3")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:3.0.3")
    implementation("org.glassfish.jersey.media:jersey-media-multipart:3.0.3")
    implementation("org.glassfish.jersey.inject:jersey-hk2:3.0.3")
    implementation("org.apache.oltu.oauth2:org.apache.oltu.oauth2.client:1.0.2")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("org.bouncycastle:bcpkix-jdk15on:1.58")


    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
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
