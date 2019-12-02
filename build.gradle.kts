import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "1.3.50"
    id("org.danilopianini.git-sensitive-semantic-versioning") version "0.2.2"
    id("com.gradle.plugin-publish") version "0.10.1"
    id("org.jetbrains.dokka") version "0.10.0"
    `maven-publish`
}

group = "dev.reimer"
version = gitSemVer.computeGitSemVer()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleKotlinDsl())
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
        testLogging {
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
            events(*TestLogEvent.values())
            exceptionFormat = TestExceptionFormat.FULL
        }
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<DokkaTask> {
        outputDirectory = "$buildDir/javadoc"
        outputFormat = "javadoc"
    }
}

pluginBundle {
    website = "https://github.com/reimersoftware/latex-gradle-plugin"
    vcsUrl = "https://github.com/reimersoftware/latex-gradle-plugin.git"
    tags = listOf("maven", "maven central", "ossrh", "central", "publish")
}

gradlePlugin {
    plugins {
        create(name) {
            id = "dev.reimer.tex"
            displayName = "TeX Gradle Plugin"
            description = "A plugin for compiling TeX."
            implementationClass = "dev.reimer.tex.gradle.plugin.TexPlugin"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
