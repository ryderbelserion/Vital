plugins {
    alias(libs.plugins.shadow)

    `kotlin-plugin`
}

dependencies {
    api(projects.core)

    api(libs.logback)
    api(libs.jda.ktx)
    api(libs.jda)
}

tasks {
    shadowJar {
        mergeServiceFiles()

        manifest {
            from(project(":core").tasks.named<Jar>("shadowJar").get().manifest)
        }
    }
}