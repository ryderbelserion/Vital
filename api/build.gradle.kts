project.version = "0.0.3"

repositories {
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnlyApi(libs.bundles.adventure)

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

                group = project.group
                artifactId = project.name.lowercase()
                version = "${project.version}"
            }
        }
    }
}