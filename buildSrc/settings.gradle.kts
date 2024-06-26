rootProject.name = "buildSrc"

dependencyResolutionManagement {
    repositories {
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
        gradlePluginPortal()
    }
}