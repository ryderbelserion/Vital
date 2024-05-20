plugins {
    id("io.github.goooler.shadow")

    `java-library`

    `maven-publish`
}

repositories {
    maven("https://jitpack.io")

    mavenCentral()
}

dependencies {
    compileOnly("net.kyori", "adventure-text-minimessage", "4.17.0")

    compileOnly("net.kyori", "adventure-api", "4.17.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val javaComponent: SoftwareComponent = components["java"]

tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
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

                artifact(sourcesJar)
                artifact(javadocJar)

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

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}