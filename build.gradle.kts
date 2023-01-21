@file:Suppress("PropertyName")

plugins {
    id("org.gradle.application")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
}

group = "flow.proxy.rutracker"
version = "3.1.0"

application {
    mainClass.set("flow.proxy.rutracker.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.ktor.server)
    implementation(libs.jsoup)
    implementation(libs.koin)
    implementation(libs.logback.classic)
}
