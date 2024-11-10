plugins {
    alias(libs.plugins.shadow)
}

project.version = "1.0.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public")
}

dependencies {
    compileOnly(libs.paper)

    api(project(":paper"))
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    javadoc {
        options.quiet()
    }

    assemble {
        dependsOn(shadowJar)

        doLast {
            copy {
                from(shadowJar.get())
                into(rootProject.projectDir.resolve("jars"))
            }
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
    }

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

    processResources {
        inputs.properties("name" to rootProject.name)
        inputs.properties("version" to project.version)
        inputs.properties("group" to project.group)
        inputs.properties("apiVersion" to libs.versions.minecraft.get())
        inputs.properties("description" to project.description)
        inputs.properties("website" to "https://modrinth.com/plugin/vital")

        filesMatching("paper-plugin.yml") {
            expand(inputs.properties)
        }
    }
}