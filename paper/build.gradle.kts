plugins {
    alias(libs.plugins.shadow)

    `paper-plugin`
}

dependencies {
    compileOnly(libs.bundles.plugins)

    compileOnly(libs.papermc)

    api(project(":core")) {
        exclude("org.yaml", "snakeyaml")
    }
}