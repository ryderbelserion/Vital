plugins {
    alias(libs.plugins.shadow)

    `paper-plugin`
}

project.version = "1.8"

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

    compileJava {
        options.compilerArgs.add("--enable-preview")
    }
}