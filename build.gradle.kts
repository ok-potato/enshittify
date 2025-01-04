
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
}

group = "com"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.server.config.yaml)
    implementation(libs.server.core)
    implementation(libs.server.host.common)
    implementation(libs.server.html.builder)
    implementation(libs.server.netty)
    implementation(libs.server.statuspages)

    testImplementation(libs.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    implementation(libs.exposed.core)
    implementation(libs.logback.classic)
    implementation(libs.moshi)
    implementation(libs.jave)
    implementation(libs.jave.native.linux)
    implementation(libs.jave.native.win)
}
