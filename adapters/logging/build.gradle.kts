plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":application"))

    api(libs.slf4j)
}
