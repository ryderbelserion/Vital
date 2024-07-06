import com.ryderbelserion.feather.enums.Repository

plugins {
    id("com.ryderbelserion.feather-core")

    `maven-publish`

    `java-library`
}

repositories {
    maven("https://repo.codemc.io/repository/maven-public")

    maven(Repository.CrazyCrewReleases.url)

    maven(Repository.Jitpack.url)

    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.ADOPTIUM)
    }

    withJavadocJar()
    withSourcesJar()
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)

        options.compilerArgs.add("--enable-preview")
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }

    publishing {
        repositories {
            maven {
                url = uri("https://repo.crazycrew.us/releases")

                credentials {
                    this.username = System.getenv("gradle_username")
                    this.password = System.getenv("gradle_password")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                from(javaComponent)

                group = rootProject.group
                artifactId = project.name.lowercase()
                version = "${rootProject.version}"
            }
        }
    }
}