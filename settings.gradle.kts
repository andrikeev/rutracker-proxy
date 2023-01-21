@file:Suppress("UnstableApiUsage")

rootProject.name = "rutracker-proxy"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}
