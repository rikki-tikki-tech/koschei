plugins {
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.jib) apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        filter {
            exclude { element ->
                element.file.path.contains("generated")
            }
        }
    }

    tasks.named("ktlintCheck").configure {
        dependsOn("ktlintFormat")
    }
}

tasks.create("assemble").dependsOn(":server:installDist")
