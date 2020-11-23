import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "0.4.22"
    java
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("org.jetbrains.dokka") version "0.10.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

intellij {
    version = "2020.2.3"
    downloadSources = false
    setPlugins("com.intellij.java", "PythonCore:202.7660.27", "org.jetbrains.kotlin:1.3.72-release-IJ2020.1-1")
}

ktlint {
    enableExperimentalRules.set(true)
}

group = "io.github.nbirillo.dataset.anonymizer"
version = "1.0-SNAPSHOT"

apply {
    plugin("kotlin")
    plugin("java")
    plugin("org.jetbrains.intellij")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // TODO: publish transformation plugin
    implementation(fileTree("libs") { include("*.jar") })
    implementation("de.mpicbg.scicomp:krangl:0.9.1")
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
}

repositories {
    mavenCentral()
    jcenter()
    flatDir {
        dirs("libs")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    runIde {
        val input: String? by project
        args = listOfNotNull(
            "anonymizer",
            input?.let { "--path=$it" }
        )
        jvmArgs = listOf("-Djava.awt.headless=true")
        standardInput = System.`in`
        standardOutput = System.`out`
    }

    register("cli") {
        dependsOn("runIde")
    }
}
