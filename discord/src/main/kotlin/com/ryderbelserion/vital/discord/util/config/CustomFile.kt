package com.ryderbelserion.vital.discord.util.config

import com.ryderbelserion.vital.core.Vital
import org.simpleyaml.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.util.function.Consumer
import java.util.logging.Level

/**
 * Builds a custom file to load with the File Manager
 *
 * @author Ryder Belserion
 * @version 1.8.4
 * @since 1.8.4
 */
public class CustomFile(private val directory: File) {

    private val api = Vital.api()
    private val logger = this.api.logger
    private val isLogging = this.api.isLogging

    /**
     * Gets the file configuration.
     *
     * @return the current configuration
     */
    private var configuration: YamlConfiguration? = null

    /**
     * Gets the name of the file without the .yml extension.
     *
     * @return the name of the file without .yml
     */
    public var strippedName: String = ""

    /**
     * Gets the file name.
     *
     * @return the name of the file
     */
    private var fileName: String = ""

    /**
     * Gets the [File].
     *
     * @return the [File]
     */
    private var file: File? = null

    /**
     * Populates the data in the class.
     *
     * @param fileName the name of the file
     * @return [CustomFile]
     */
    public fun apply(fileName: String): CustomFile? {
        if (fileName.isEmpty()) {
            listOf(
                "The file name cannot be empty!",
                "File Name: $fileName"
            ).forEach(Consumer { msg: String? -> this.logger.severe(msg) })

            return null
        }

        this.strippedName = fileName.replace(".yml", "")
        this.fileName = fileName

        this.file = File(this.directory, this.fileName)

        try {
            if (this.isLogging) this.logger.info("Loading " + this.strippedName + ".yml...")

            this.configuration = YamlConfiguration.loadConfiguration(this.file)
        } catch (exception: Exception) {
            this.logger.log(Level.SEVERE, "Failed to load or create " + this.strippedName + ".yml...", exception)
        }

        return this
    }

    /**
     * Checks if the configuration is null.
     *
     * @return true or false
     */
    public fun exists(): Boolean {
        return this.configuration != null
    }

    /**
     * Saves a custom configuration.
     */
    public fun save() {
        if (this.fileName.isEmpty()) return

        if (!exists()) return

        try {
            this.configuration?.save(this.file)
        } catch (exception: IOException) {
            this.logger.log(Level.SEVERE, "Could not save " + this.strippedName + ".yml...", exception)
        }
    }

    /**
     * Reloads a custom configuration.
     */
    public fun reload() {
        if (this.fileName.isEmpty()) return

        if (!exists()) return

        try {
            this.configuration = YamlConfiguration.loadConfiguration(this.file)
        } catch (exception: Exception) {
            this.logger.log(Level.SEVERE, "Could not reload the " + this.strippedName + ".yml...", exception)
        }
    }
}