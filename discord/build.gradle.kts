plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

project.version = "0.0.1"

dependencies {
    api(libs.logback)

    api(libs.jda)
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