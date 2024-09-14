plugins {
    `velocity-plugin`
}

project.version = "0.0.1"

dependencies {
    annotationProcessor(libs.velocity)
    compileOnly(libs.velocity)

    api(project(":common"))
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