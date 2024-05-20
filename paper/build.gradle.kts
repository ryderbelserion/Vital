plugins {
    `paper-plugin`
}

dependencies {
    compileOnly("com.github.LoneDev6", "api-itemsadder", "3.6.3-beta-14")

    compileOnly("com.arcaniax", "HeadDatabase-API", "1.3.1")

    compileOnly("me.clip", "placeholderapi", "2.11.5")

    compileOnly("io.th0rgal", "oraxen", "1.171.0")

    api(project(":core"))
}