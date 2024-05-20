plugins {
    id("io.github.goooler.shadow")

    kotlin("jvm")
}

repositories {
    maven("https://jitpack.io")

    mavenCentral()
}

kotlin {
    jvmToolchain(21)

    explicitApi()
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "21"
            javaParameters = true
        }
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}