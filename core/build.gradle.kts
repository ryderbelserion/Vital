plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

dependencies {
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.annotations)

    api(libs.configme)
}