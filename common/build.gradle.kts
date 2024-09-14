plugins {
    `java-plugin`
}

project.version = "0.0.1"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.jetbrains)
    compileOnly(libs.brigadier)

    compileOnly(libs.gson)

    api(libs.jalu)
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(javaComponent)

                group = rootProject.group
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }
}