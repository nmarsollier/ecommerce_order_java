plugins {
    id("java")
}

group = "com.order"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.javalin:javalin:5.6.1")
    implementation("org.mongodb:mongodb-driver-sync:4.10.2")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.rabbitmq:amqp-client:5.16.0")
    implementation("com.google.guava:guava:30.1-jre")

    implementation("com.google.dagger:dagger:2.16")
    annotationProcessor("com.google.dagger:dagger-compiler:2.16")

    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.slf4j:slf4j-simple:2.0.7")
}

sourceSets {
    main {
        java {
            srcDir("${buildDir.absolutePath}/generated/sources/annotationProccessor/java/main")
        }
    }
}

