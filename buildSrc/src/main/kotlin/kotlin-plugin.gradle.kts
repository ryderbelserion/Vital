import com.ryderbelserion.feather.enums.Repository
import gradle.kotlin.dsl.accessors._5eb79900c8ae59d7bb3a62f4246cfb92.feather

plugins {
    id("com.ryderbelserion.feather-core")

    kotlin("jvm")
}

repositories {
    mavenCentral()
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