plugins {
    id("java")
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    id("application")
}

group = "com.order"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass = "com.order.Server"
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19)) // Set to the desired Java version
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.rabbitmq:amqp-client:5.16.0")
    implementation("com.google.guava:guava:30.1-jre")
    implementation("com.google.code.gson:gson:2.10.1")
}

sourceSets {
    main {
        java {
            srcDir("${buildDir.absolutePath}/generated/sources/annotationProccessor/java/main")
        }
    }
}

