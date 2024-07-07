enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Vital"

pluginManagement {
    repositories {
        maven("https://repo.crazycrew.us/releases")

        gradlePluginPortal()
    }
}

plugins {
    id("com.ryderbelserion.feather-settings") version "0.0.3"
}

include("example")
include("discord")
include("paper")
include("core")