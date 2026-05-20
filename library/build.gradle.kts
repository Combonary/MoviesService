import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    `maven-publish`
}

group = "io.github.combonary"
version = "1.0.7"

kotlin {
    jvm()
    android {
        namespace = "com.combonary.tmdb"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()

    sourceSets {
        commonMain {
            kotlin.srcDir(layout.buildDirectory.dir("generated/ksp/metadata/commonMain/kotlin"))
            kotlin.exclude("**/*_Impl.kt")
            kotlin.exclude("**/MovieDatabaseConstructor.kt")
            dependencies {
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktorfit.lib)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
// Load local.properties
val localProperties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Combonary/MoviesService")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                    ?: localProperties.getProperty("gpr.user")
                password = System.getenv("GITHUB_TOKEN")
                    ?: localProperties.getProperty("gpr.key")
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
    add("kspLinuxX64", libs.room.compiler)
}

tasks.matching { it.name.contains("compile", ignoreCase = true) || (it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata") }.configureEach {
    mustRunAfter(tasks.matching { it.name == "kspCommonMainKotlinMetadata" })
    dependsOn(tasks.matching { it.name == "kspCommonMainKotlinMetadata" })
}

tasks.named("sourcesJar").configure {
    dependsOn(tasks.named("kspCommonMainKotlinMetadata"))
}

room {
    schemaDirectory("$projectDir/schemas")
}

ktorfit {
    compilerPluginVersion = "2.3.4"
}
