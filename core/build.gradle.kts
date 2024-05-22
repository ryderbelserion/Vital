plugins {
    `java-plugin`
}

dependencies {
    compileOnly(libs.minimessage)
    compileOnly(libs.adventure)

    api(libs.configme)
    api(libs.yaml)
}