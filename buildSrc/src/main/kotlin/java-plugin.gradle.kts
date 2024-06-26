import com.ryderbelserion.feather.enums.Repository

plugins {
    id("com.ryderbelserion.feather-core")

    `maven-publish`
    `java-library`
}

java {
    withJavadocJar()
    withSourcesJar()
}

feather {
    repository("https://repo.codemc.io/repository/maven-public")

    repository(Repository.CrazyCrewReleases.url)

    repository(Repository.Jitpack.url)

    configureJava {
        javaSource(JvmVendorSpec.ADOPTIUM)

        javaVersion(21)
    }
}

tasks {
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
    }
}