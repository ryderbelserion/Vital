import com.ryderbelserion.feather.feather

plugins {
    id("com.ryderbelserion.feather-logic") version "0.0.3"

    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin)

    feather("0.0.3")
}