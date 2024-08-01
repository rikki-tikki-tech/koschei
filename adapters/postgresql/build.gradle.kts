plugins {
    `java-library`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.jooq)
    alias(libs.plugins.flyway)
}

dependencies {
    implementation(project(":domain"))

    api(libs.flywayCore)
    api(libs.hikariCP)
    api(libs.jooq)
    api(libs.postgresql)
    api(libs.kotlinx.coroutines.core)

    jooqGenerator(libs.postgresql)
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.14.0")
    }
}

flyway {
    url = System.getenv("JDBC_URL")
    user = System.getenv("JDBC_USERNAME")
    password = System.getenv("JDBC_PASSWORD")
}

jooq {
    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(false)
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = System.getenv("JDBC_URL")
                    user = System.getenv("JDBC_USERNAME")
                    password = System.getenv("JDBC_PASSWORD")
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        excludes = "flyway_schema_history"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isRelations = true
                        isDeprecated = false
                        isRecords = true
                        isPojos = true
                        isPojosEqualsAndHashCode = true
                        isDaos = true
                        isKotlinNotNullRecordAttributes = true
                        isKotlinNotNullInterfaceAttributes = true
                        isKotlinNotNullPojoAttributes = true
                        isJavaTimeTypes = true
                    }
                    target.apply {
                        packageName = "adapters.postgresql.generated"
                        directory = "generated"
                    }
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") {
    inputs.dir(file("$projectDir/src/main/resources/db/migration"))
    outputs.cacheIf { true }
}

tasks.named("flywayMigrate") {
    finalizedBy("generateJooq")
}
