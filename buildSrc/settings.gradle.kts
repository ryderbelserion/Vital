rootProject.name = "buildSrc"

dependencyResolutionManagement {
    repositories {
        maven("https://repo.crazycrew.us/releases")

        gradlePluginPortal()

        mavenCentral()
    }

    versionCatalogs {
        register("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    repositories {
        maven("https://repo.crazycrew.us/releases")

        gradlePluginPortal()
    }
}

plugins {
    id("com.ryderbelserion.feather-settings")
}