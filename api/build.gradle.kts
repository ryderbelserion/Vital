plugins {
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnlyApi(libs.bundles.adventure)

    compileOnly(libs.configurate.yaml)

    compileOnly(libs.brigadier)

    compileOnly(libs.gson)
}