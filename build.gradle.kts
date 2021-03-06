repositories {
    mavenCentral()
}

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("java-library")
    id("war")
}

group = "de.kernebeck.escaperoom"
version = "0.0.1-SNAPSHOT"


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.mariadb.jdbc:mariadb-java-client")
    implementation("org.flywaydb:flyway-core")
    implementation("commons-io:commons-io:2.9.0")
    implementation("com.google.guava:guava:30.1.1-jre")

    implementation("org.apache.wicket:wicket-core:9.4.0")
    implementation("org.apache.wicket:wicket-spring:9.4.0")
    implementation("org.apache.wicket:wicket-native-websocket-javax:9.4.0")
    implementation("org.apache.wicket:wicket-bean-validation:9.4.0")
    implementation("org.apache.wicket:wicket-extensions:9.4.0")


    developmentOnly("org.apache.wicket:wicket-devutils:9.4.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")


    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.mockito:mockito-core:3.12.2")
    testImplementation("org.mockito:mockito-junit-jupiter:3.12.2")
    testImplementation("org.testcontainers:testcontainers:1.16.0")
    testImplementation("org.testcontainers:mariadb:1.16.0")
    testImplementation("org.assertj:assertj-core:3.20.2")
}

sourceSets {
    main {
        java.srcDir("src/main/java")
        resources.srcDir("src/main/java")
    }
    test {
        java.srcDir("src/test/java")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


tasks.withType<Test> {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    useJUnitPlatform()
    filter.isFailOnNoMatchingTests = false
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
}