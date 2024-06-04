plugins {
    alias(libs.plugins.shadow)

    `paper-plugin`
}

dependencies {
    compileOnlyApi(libs.bundles.plugins)

    api(projects.core)
}

tasks {
    shadowJar {
        mergeServiceFiles()

        manifest {
            from(project(":core").tasks.named<Jar>("shadowJar").get().manifest)
        }
    }
}