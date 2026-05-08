import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

group = "Combonary"
version = "1.0.1"

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

mavenPublishing {
    // publishToMavenCentral()

    //signAllPublications()

    coordinates(group.toString(), "MoviesService", version.toString())

    pom {
        name = "Tmdb service"
        description = "A Kotlin Multiplatform library for TMDB API integration."
        inceptionYear = "2026"
        url = "https://github.com/combonary/MoviesService/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "Combonary"
                name = "Pascal Adjaero"
                url = "https://github.com/Combonary/"
            }
        }
        scm {
            url = "https://github.com/Combonary/MoviesService/"
            connection = "scm:git:git://github.com/Combonary/MoviesService.git"
            developerConnection = "scm:git:ssh://github.com/Combonary/MoviesService.git"
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Combonary/MoviesService")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
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


room {
    schemaDirectory("$projectDir/schemas")
}

ktorfit {
    compilerPluginVersion = "2.3.4"
}
