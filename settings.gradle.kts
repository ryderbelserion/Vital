rootProject.name = "Vital"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }

        gradlePluginPortal()
        mavenCentral()
    }
}

include("velocity", "discord", "common", "paper", "example")