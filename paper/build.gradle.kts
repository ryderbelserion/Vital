plugins {
    alias(libs.plugins.shadow)

    `paper-plugin`
}

dependencies {
    compileOnlyApi(libs.bundles.plugins)

    compileOnly(libs.papermc)

    api(projects.core)
}

tasks {
    shadowJar {
        manifest {
            from(project(":core").tasks.named<Jar>("shadowJar").get().manifest)
        }
    }
}