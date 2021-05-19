repositories {
    mavenCentral()
}

plugins {
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.siouan.frontend-jdk11") version "5.1.0"
    id("java-library")
    id("war")
}

group = "de.kernebeck.escaperoom"
version = "0.0.1-SNAPSHOT"

apply(plugin = "org.siouan.frontend-jdk11")


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.mariadb.jdbc:mariadb-java-client")
    implementation("org.flywaydb:flyway-core")



    developmentOnly("org.springframework.boot:spring-boot-devtools")


    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

sourceSets {
    main {
        java.srcDir("src/main/java")
    }
    test {
        java.srcDir("src/test/java")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

frontend {
    nodeVersion.set("14.16.1")
    assembleScript.set("run build")
}


tasks.withType<Test> {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    useJUnitPlatform()
    filter.isFailOnNoMatchingTests = false
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
}