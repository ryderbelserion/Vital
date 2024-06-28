import com.ryderbelserion.feather.enums.Repository

plugins {
    id("java-plugin")
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi")

    maven("https://repo.codemc.io/repository/maven-public")

    maven("https://repo.triumphteam.dev/snapshots")

    maven("https://repo.oraxen.com/releases")

    maven(Repository.Paper.url)
}