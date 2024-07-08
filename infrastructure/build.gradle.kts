plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jib)
    alias(libs.plugins.protobuf)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))
    implementation(project(":adapters:postgresql"))
    implementation(project(":adapters:logging"))
    implementation(project(":adapters:config"))
    implementation(project(":adapters:authentication"))
    implementation(project(":ports:grpc"))

    api(libs.kotlinx.coroutines.core)

    api(libs.koin.core)
    api(libs.koin.core.ext)
    api(libs.koin.logger.slf4j)

    api(libs.slf4j)
    api(libs.logback)

    api(libs.config)
    api(libs.dotenv.kotlin)

    runtimeOnly(libs.grpc.netty)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.grpc.testing)
}

tasks.register<JavaExec>("HelloWorldServer") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("infrastructure.ApplicationKt")
}

val helloWorldServerStartScripts =
    tasks.register<CreateStartScripts>("helloWorldServerStartScripts") {
        mainClass.set("infrastructure.ApplicationKt")
        applicationName = "hello-world-server"
        outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
        classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
    }

tasks.named("startScripts") {
    dependsOn(helloWorldServerStartScripts)
}

tasks.withType<Test> {
    useJUnit()

    testLogging {
        events =
            setOf(
                org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
                org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showStandardStreams = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

jib {
    from {
        image = "gcr.io/distroless/java17-debian12"
    }
    to {
        image = "koschei"
        auth {
            username = providers.environmentVariable("DOCKERHUB_USERNAME").get()
            password = providers.environmentVariable("DOCKERHUB_TOKEN").get()
        }
    }
    container {
        mainClass = "infrastructure.ApplicationKt"
        ports = listOf("50051")
    }
}
