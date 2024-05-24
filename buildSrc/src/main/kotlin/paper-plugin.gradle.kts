import com.ryderbelserion.feather.enums.Repository
import org.gradle.accessors.dm.LibrariesForLibs

val libs = the<LibrariesForLibs>()

plugins {
    id("java-plugin")
}

feather {
    repository("https://repo.extendedclip.com/content/repositories/placeholderapi")

    repository("https://repo.papermc.io/repository/maven-public")

    repository("https://repo.codemc.io/repository/maven-public")

    repository("https://repo.triumphteam.dev/snapshots")

    repository("https://repo.oraxen.com/releases")

    repository(Repository.Paper.url)
}

dependencies {
    compileOnly(libs.papermc)
}