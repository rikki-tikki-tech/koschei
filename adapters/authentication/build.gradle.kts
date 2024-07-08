plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":application"))

    api(libs.bcrypt)
    api(libs.java.jwt)
}
