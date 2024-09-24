plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

project.version = "0.0.2"

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

                group = rootProject.group
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }
}