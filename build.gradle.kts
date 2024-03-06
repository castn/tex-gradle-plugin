import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.2.1"
    id("org.jetbrains.dokka") version "0.10.0"
    `maven-publish`
    id("com.palantir.git-version") version "3.0.0"
}

val gitVersion: groovy.lang.Closure<String> by extra
group = "dev.reimer"
version = gitVersion()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(gradleKotlinDsl())
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.4.2")
}

lateinit var javadocJar: TaskProvider<Jar>
lateinit var sourcesJar: TaskProvider<Jar>

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

    // Include project license in generated JARs.
    withType<Jar> {
        from(project.projectDir) {
            include("LICENSE")
            into("META-INF")
        }
    }

    // Generate Kotlin/Java documentation from sources.
    dokka {
        outputFormat = "html"
    }

    // JAR containing Kotlin/Java documentation.
    javadocJar = register<Jar>("javadocJar") {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        dependsOn(dokka)
        from(dokka)
        archiveClassifier.set("javadoc")
    }

    // JAR containing all source files.
    sourcesJar = register<Jar>("sourcesJar") {
        from(sourceSets.main.get().allSource)
        archiveClassifier.set("sources")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(javadocJar.get())
        }
    }
}

gradlePlugin {
    website = "https://github.com/reimersoftware/tex-gradle-plugin"
    vcsUrl = "https://github.com/reimersoftware/tex-gradle-plugin.git"
    plugins {
        create(name) {
            id = "dev.reimer.tex"
            implementationClass = "dev.reimer.tex.gradle.plugin.TexPlugin"
            displayName = "TeX Gradle Plugin"
            description = "A plugin for compiling TeX."
            tags = listOf(
                "tex",
                "latex",
                "pdflatex",
                "bibtex",
                "bibliography",
                "biblatex",
                "gradle",
                "gradle-plugin"
            )
        }
    }
}
