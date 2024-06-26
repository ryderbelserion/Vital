import com.ryderbelserion.feather.tools.latestCommitHash

plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

project.version = "1.8"

dependencies {
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.annotations)

    api(libs.configme)
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
            attributes["Git-Commit"] = latestCommitHash()
        }
    }
}