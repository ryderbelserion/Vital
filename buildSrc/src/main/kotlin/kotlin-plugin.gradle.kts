import com.ryderbelserion.feather.enums.Repository
import gradle.kotlin.dsl.accessors._5eb79900c8ae59d7bb3a62f4246cfb92.feather
import gradle.kotlin.dsl.accessors._5eb79900c8ae59d7bb3a62f4246cfb92.publishing

plugins {
    id("com.ryderbelserion.feather-core")

    `maven-publish`

    kotlin("jvm")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
}

feather {
    repository("https://repo.codemc.io/repository/maven-public")

    repository(Repository.CrazyCrewReleases.url)

    repository(Repository.Jitpack.url)

    configureKotlin {
        javaSource(JvmVendorSpec.ADOPTIUM)

        javaVersion(21)
    }
}

tasks {
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