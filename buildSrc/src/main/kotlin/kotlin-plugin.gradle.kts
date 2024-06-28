import com.ryderbelserion.feather.enums.Repository
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.ryderbelserion.feather-core")

    `maven-publish`

    kotlin("jvm")
}

repositories {
    maven("https://repo.codemc.io/repository/maven-public")

    maven(Repository.CrazyCrewReleases.url)

    maven(Repository.Jitpack.url)

    mavenCentral()
}

kotlin {
    jvmToolchain(21)

    explicitApi()
}

tasks {
    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
            javaParameters = true
        }
    }

    publishing {
        repositories {
            maven {
                url = uri("https://repo.crazycrew.us/releases")

                credentials {
                    this.username = System.getenv("gradle_username")
                    this.password = System.getenv("gradle_password")
                }
            }
        }
    }
}