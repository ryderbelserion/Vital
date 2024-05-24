plugins {
    `java-plugin`
}

dependencies {
    compileOnly(libs.bundles.adventure)

    compileOnly(libs.annotations)

    api(libs.configme)
    api(libs.yaml)
}