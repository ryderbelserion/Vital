plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)

    `paper-plugin`
}

dependencies {
    api(projects.paper)

    implementation(libs.triumph.cmds)

    compileOnly(libs.papermc)
}

tasks {
    runServer {
        jvmArgs("-Dnet.kyori.ansi.colorLevel=truecolor")

        defaultCharacterEncoding = Charsets.UTF_8.name()

        minecraftVersion("1.21")
    }
}