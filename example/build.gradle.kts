plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    `paper-plugin`
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    implementation(project(":paper"))
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion("1.21.1")
    }

    javadoc {
        options.quiet()
    }
}