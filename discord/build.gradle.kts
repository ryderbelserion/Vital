plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

project.version = "1.8.2"

dependencies {
    api(projects.core)

    api(libs.logback)

    api(libs.yaml) {
        exclude("org.yaml", "snakeyaml")
    }

    api(libs.jda)
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(javaComponent)

                group = "com.ryderbelserion.vital"
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }

    shadowJar {
        mergeServiceFiles()

        manifest {
            from(project(":core").tasks.named<Jar>("shadowJar").get().manifest)
        }
    }
}