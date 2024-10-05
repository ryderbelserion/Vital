project.version = "0.0.1"

repositories {
    maven("https://repo.papermc.io/repository/maven-public")
}

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

                group = project.group
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }
}