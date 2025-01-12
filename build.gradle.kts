
plugins {
    alias(libs.plugins.jooq.codegen.gradle)
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

    implementation(libs.logback.classic)
    implementation(libs.moshi)
    implementation(libs.jave)
    implementation(libs.jave.native.linux)
    implementation(libs.jave.native.win)
    implementation(libs.postgresql)

    implementation(libs.jooq)
    jooqCodegen(libs.jooq.codegen)
    jooqCodegen(libs.jooq.meta)
    jooqCodegen(libs.postgresql)
}

jooq {
    configuration {
        jdbc {
            driver = "org.postgresql.Driver"
            url = "jdbc:postgresql://localhost:5432/enshittify"
            user = "postgres"
            password = "1234"
        }

        generator {
            name = "org.jooq.codegen.KotlinGenerator"

            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                includes = ".*"
                excludes = ""
                inputSchema = "public"
            }

            generate {}

            target {
                packageName = "generated.jooq"
                directory = "/src/main/kotlin"
            }
        }
    }
}
