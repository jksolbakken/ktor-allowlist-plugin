import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

group = "no.jksolbakken"
version = System.getenv("VERSION") ?: "notimportant"

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
    jvmToolchain(21)
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
        gradleVersion = "9.8.0"
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "no.jksolbakken"
            artifactId = "ktor-allowlist-plugin"
            version = version

            from(components["java"])
        }
    }
}
