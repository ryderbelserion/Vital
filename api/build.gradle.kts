repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnlyApi(libs.bundles.adventure)

    compileOnly(libs.brigadier)

    api(libs.jalu)
}