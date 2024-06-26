plugins {
    alias(libs.plugins.shadow)

    `paper-plugin`
}

project.version = "1.8"

dependencies {
    compileOnlyApi(libs.bundles.plugins)

    compileOnly(libs.papermc)

    api(projects.core)
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

    compileJava {
        options.compilerArgs.add("--enable-preview")
    }
}