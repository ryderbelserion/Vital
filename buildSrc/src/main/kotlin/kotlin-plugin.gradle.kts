import com.ryderbelserion.feather.enums.Repository
import gradle.kotlin.dsl.accessors._5eb79900c8ae59d7bb3a62f4246cfb92.feather
import gradle.kotlin.dsl.accessors._5eb79900c8ae59d7bb3a62f4246cfb92.publishing

plugins {
    id("com.ryderbelserion.feather-core")

    `maven-publish`

    kotlin("jvm")
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
}

feather {
    repository("https://repo.codemc.io/repository/maven-public")

    repository(Repository.CrazyCrewReleases.url)

    repository(Repository.Jitpack.url)

    configureKotlin {
        javaSource(JvmVendorSpec.ADOPTIUM)

        javaVersion(21)
    }
}

val javaComponent: SoftwareComponent = components["java"]

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

        publications {
            create<MavenPublication>("maven") {
                from(javaComponent)

                group = "com.ryderbelserion.vital"
                artifactId = project.name.lowercase()
                version = "${project.version}"

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }

                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }

                pom {
                    name.set("Vital API")
                    description.set("A library with a slew of things")
                    url.set("https://github.com/ryderbelserion/Vital")

                    licenses {
                        licenses {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }

                    developers {
                        developer {
                            id.set("ryderbelserion")
                            name.set("Ryder Belserion")
                            email.set("no-reply@ryderbelserion.com")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/ryderbelserion/Vital")
                        developerConnection.set("scm:git:ssh://github.com/ryderbelserion/Vital")
                        url.set("https://github.com/ryderbelserion/Vital")
                    }
                }
            }
        }
    }
}