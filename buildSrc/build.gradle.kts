import com.ryderbelserion.feather.feather

plugins {
    id("com.ryderbelserion.feather-logic") version "0.0.1"

    `kotlin-dsl`
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.kotlin)

    feather("0.0.1")
}