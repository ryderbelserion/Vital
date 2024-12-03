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

val javaComponent: SoftwareComponent = components["java"]

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(javaComponent)

                group = project.group
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }
}