plugins {
    alias(libs.plugins.shadow)
}

project.version = "0.0.3"

dependencies {
    api(project(":common"))

    api(libs.logback)

    api(libs.gson)

    api(libs.jda)
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