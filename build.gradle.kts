import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "io.github.nbirillo.dataset.anonymizer"
version = "1.0-SNAPSHOT"

plugins {
    id("org.jetbrains.intellij") version "0.4.21"
    java
    kotlin("jvm") version "1.4.10"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("org.jetbrains.dokka") version "0.10.1"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
}

repositories {
    mavenCentral()
    jcenter()
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // TODO: publish transformation plugin
    implementation(fileTree("libs") { include("*.jar") })
    implementation(group = "de.mpicbg.scicomp", name = "krangl", version = "0.9.1")
    implementation(group = "com.xenomachina", name = "kotlin-argparser", version = "2.0.7")
}

intellij {
    version = "2020.2.3"
    downloadSources = false
    setPlugins("com.intellij.java", "PythonCore:202.7660.27", "org.jetbrains.kotlin")
}

ktlint {
    enableExperimentalRules.set(true)
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
        jvmArgs = listOf("-Djava.awt.headless=true", "--add-exports", "java.base/jdk.internal.vm=ALL-UNNAMED")
        standardInput = System.`in`
        standardOutput = System.`out`
    }

    register("cli") {
        dependsOn("runIde")
    }
}
