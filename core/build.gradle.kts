import com.ryderbelserion.feather.tools.latestCommitMessage

plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

dependencies {
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.annotations)

    compileOnly(libs.configme)
}

tasks {
    shadowJar {
        mergeServiceFiles()

        manifest {
            attributes["Git-Message"] = latestCommitMessage()
            attributes["Git-Commit"] = rootProject.version
        }
    }
}