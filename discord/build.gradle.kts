plugins {
    alias(libs.plugins.shadow)

    `kotlin-plugin`
}

project.version = "1.8.3"

dependencies {
    api(kotlin("stdlib"))

    api(projects.core) {
        exclude("com.github.LoneDev6")
        exclude("io.th0rgal")
        exclude("net.kyori")
        exclude("me.clip")
    }

    api(libs.logback)

    api(libs.jda.ktx) {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
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