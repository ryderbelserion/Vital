plugins {
    alias(libs.plugins.paperweight)
    alias(libs.plugins.shadow)

    `paper-plugin`
}

project.version = "0.0.8"

dependencies {
    paperweight.paperDevBundle(libs.versions.paper)

    compileOnly(libs.bundles.plugins) {
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

                group = rootProject.group
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }
}