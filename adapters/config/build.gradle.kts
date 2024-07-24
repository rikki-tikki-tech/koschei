plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":adapters:postgresql"))
    implementation(project(":adapters:authentication"))
    implementation(project(":adapters:logging"))
    implementation(project(":adapters:google-oauth"))
    implementation(project(":ports:grpc"))

    api(libs.koin.core)
    implementation(libs.reflections)

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xopt-in=org.koin.core.annotation.KoinInternalApi",
                "-Xopt-in=org.koin.core.annotation.KoinReflectAPI",
            )
        }
    }
}
