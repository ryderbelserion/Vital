plugins {
    `paper-plugin`
}

dependencies {
    compileOnly(libs.bundles.plugins)

    api(projects.core)
}