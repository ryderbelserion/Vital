plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)
}

project.version = "1.0.7"

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.papermc.io/repository/maven-public")

    maven("https://repo.oraxen.com/releases")
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.bundles.shared) {
        exclude("org.bukkit", "*")
    }

    api(project(":common")) {
        exclude("org.yaml")
    }
}

paperweight {
    reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(javaComponent)

                group = project.group
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }
}