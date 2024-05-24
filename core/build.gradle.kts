plugins {
    `java-plugin`
}

dependencies {
    compileOnly(libs.bundles.adventure)

    api(libs.configme)
    api(libs.yaml)
}