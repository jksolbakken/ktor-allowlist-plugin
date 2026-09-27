import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

group = "no.jksolbakken"
version = System.getenv("PROJ_VERSION") ?: "notimportant"

repositories {
    mavenCentral()
}

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.maven.publish)
}

dependencies {
    implementation(libs.ktor.server)

    testImplementation(libs.test.junit5)
    testImplementation(kotlin("test"))
    testImplementation(libs.test.ktor.server)

    testRuntimeOnly(libs.test.junit.platform)
}

kotlin {
    jvmToolchain(25)
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            showExceptions = true
        }
        testLogging {
            exceptionFormat = FULL
        }
    }

    withType<Wrapper> {
        gradleVersion = "9.7.1"
    }
}
