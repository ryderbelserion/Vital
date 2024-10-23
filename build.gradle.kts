plugins {
    `maven-publish`

    `java-library`
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "java-library")

    group = "com.ryderbelserion.vital"
    description = "A library carefully crafted, as not to be all over the place."

    repositories {
        maven("https://repo.codemc.io/repository/maven-public")

        maven("https://repo.crazycrew.us/releases")

        maven("https://jitpack.io")

        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }

        withJavadocJar()
        withSourcesJar()
    }

    tasks {
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

        publishing {
            repositories {
                maven {
                    url = uri("https://repo.crazycrew.us/libraries")

                    credentials {
                        this.username = System.getenv("gradle_username")
                        this.password = System.getenv("gradle_password")
                    }
                }
            }
        }
    }
}