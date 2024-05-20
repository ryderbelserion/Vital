plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()

    mavenCentral()
}

dependencies {
    implementation("io.github.goooler.shadow", "shadow-gradle-plugin", "8.1.7")

    implementation("io.papermc.paperweight", "paperweight-userdev", "1.7.1")

    implementation("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.9.23")
}