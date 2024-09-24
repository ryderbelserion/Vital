plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

project.version = "0.0.1"

dependencies {
    api(libs.logback)

    api(libs.jda)
}