
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("com.android.library") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.20" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("jacoco")
    id("org.sonarqube") version "4.0.0.2929"
    id ("com.github.ben-manes.versions") version "0.50.0"
}

buildscript {
    val jacocoVersion = "0.8.12"

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://repo.eclipse.org/content/repositories/paho-releases/")}
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.5.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51")
        classpath("org.jacoco:org.jacoco.core:$jacocoVersion")
    }
}