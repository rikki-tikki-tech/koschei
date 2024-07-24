plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    api(libs.google.api.client)
    api(libs.google.api.services.oauth2)
}
