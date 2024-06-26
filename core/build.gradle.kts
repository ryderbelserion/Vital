import com.ryderbelserion.feather.tools.latestCommitHash

plugins {
    alias(libs.plugins.shadow)

    `java-plugin`
}

project.version = "1.8"

dependencies {
    compileOnly(libs.bundles.adventure)
    compileOnly(libs.annotations)

    api(libs.configme)
}

tasks {
    shadowJar {
        mergeServiceFiles()

        manifest {
            attributes["Git-Commit"] = latestCommitHash()
        }
    }
}