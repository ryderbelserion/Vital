import com.ryderbelserion.feather.enums.Repository
import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("com.ryderbelserion.feather-core")

    `maven-publish`
    `java-library`
}

java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    compileOnlyApi(libs.annotations)
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
                version = "${rootProject.version}"

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