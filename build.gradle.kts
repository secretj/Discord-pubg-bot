plugins {
    kotlin("jvm") version "1.9.25" // Kotlin 1.9.x 버전
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2" // Spring Boot 3.4.2 버전
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.secretj"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21)) // JDK 21 사용
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starter 의존성들
    implementation("org.springframework.boot:spring-boot-starter:3.0.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.0.5")
    
    // Kotlin 의존성
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    
    // MySQL Connector
    implementation("com.mysql:mysql-connector-j:9.2.0")

    // Discord API (discord4j 사용)
    implementation("com.discord4j:discord4j-core:3.0.0")
    
    // .env 파일 읽기 위한 라이브러리
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.0")

    // Spring Boot 개발 도구 (DevTools)
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // 테스트 의존성
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    runtimeOnly("org.springframework.boot:spring-boot-docker-compose")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    runtimeOnly("com.mysql:mysql-connector-j:9.2.0")
}
kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}