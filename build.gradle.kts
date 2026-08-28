import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("org.springframework.boot") version "3.2.4" // 최신 안정 버전 적용
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "org.soyoyoo"
version = "1.0.5-SNAPSHOT-" + SimpleDateFormat("yyyyMMddHHmmss").format(Date())

java {
    sourceCompatibility = JavaVersion.VERSION_21 // Spring Boot 3.x 필수 사양 (Java 17 이상)
 }

repositories {
    mavenCentral() // jcenter() 제거
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencies {
    // Web & Template Engine
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-mustache")

    // Lombok (Kotlin DSL 스타일)
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    // Mustache
    // DB & JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.mariadb.jdbc:mariadb-java-client")
    runtimeOnly("com.h2database:h2") // runtimeOnly 권장

    // Security & Session
    // Security 핵심 의존성 직접 추가
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.session:spring-session-jdbc")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.jar {
    enabled = false
}