rootProject.name = "koschei"

include(
    "domain",
    "application",
    "adapters:postgresql",
    "adapters:logging",
    "adapters:authentication",
    "adapters:config",
    "adapters:google-oauth",
    "ports:grpc",
    "infrastructure",
)

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

