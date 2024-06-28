plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

dependencies {
    api(projects.core)

    api(libs.logback)

    api(libs.yaml) {
        exclude("org.yaml", "snakeyaml")
    }

    api(libs.jda)
}

tasks {
    shadowJar {
        manifest {
            from(project(":core").tasks.named<Jar>("shadowJar").get().manifest)
        }
    }
}