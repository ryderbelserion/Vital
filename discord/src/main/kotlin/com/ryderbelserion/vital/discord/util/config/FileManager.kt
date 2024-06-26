package com.ryderbelserion.vital.discord.util.config

import com.ryderbelserion.vital.core.Vital
import com.ryderbelserion.vital.core.util.FileUtil
import org.simpleyaml.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.*
import java.util.logging.Level

/**
 * A file manager that handles yml configs
 *
 * @author Ryder Belserion
 * @author BadBones69
 *
 * @version 1.8.4
 * @since 1.8.4
 */
public class FileManager {
    
    private val api = Vital.api()
    
    private val dataFolder: File = this.api.directory
    private val logger = this.api.logger
    private val isLogging = this.api.isLogging

    // Holds static files
    private val files: MutableMap<String, YamlConfiguration> = HashMap()

    // Holds the folders to load dynamic files in
    private val customFiles: MutableSet<CustomFile> = HashSet()
    private val folders: MutableSet<String> = HashSet()

    /**
     * Creates the data folder and anything else we need.
     *
     * @since 1.8.4
     */
    public fun init() {
        this.dataFolder.mkdirs()

        this.customFiles.clear()

        // Creates the custom folders.
        for (folder in this.folders) {
            val resolvedFolder = File(this.dataFolder, folder).toPath()

            if (!Files.exists(resolvedFolder)) {
                // Create directory.
                try {
                    Files.createDirectory(resolvedFolder)
                } catch (e: IOException) {
                    this.logger.severe("Failed to create directory: " + resolvedFolder.toFile().name + "...")
                }

                // extract files if needed.
                FileUtil.extracts(FileManager::class.java, "/$folder/", resolvedFolder, true)

                // get all files with recursion
                loadFiles(resolvedFolder.toFile(), folder)
            } else {
                loadFiles(resolvedFolder.toFile(), "")
            }
        }
    }

    /**
     * Reload a [YamlConfiguration].
     *
     * @param fileName the name of the [YamlConfiguration] to save
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun reloadFile(fileName: String): FileManager {
        if (fileName.isEmpty()) return this

        val key = File(this.dataFolder, fileName)

        try {
            this.files[fileName] = YamlConfiguration.loadConfiguration(key)
        } catch (exception: Exception) {
            this.logger.log(Level.SEVERE, "Failed to load: $fileName...", exception)
        }

        return this
    }

    /**
     * Adds a [YamlConfiguration].
     *
     * @param fileName the name of the [YamlConfiguration] to add
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun addFile(fileName: String): FileManager {
        if (fileName.isEmpty()) return this

        val file = File(this.dataFolder, fileName)

        try {
            if (!file.exists()) {
                FileUtil.extract(FileManager::class.java, fileName, this.dataFolder.toPath(), false)

                if (this.isLogging) this.logger.info("Copied $fileName because it did not exist...")
            } else {
                if (this.isLogging) this.logger.info("Loading the file $fileName...")
            }

            // Add other file
            this.files[fileName] = YamlConfiguration.loadConfiguration(file)
        } catch (exception: Exception) {
            this.logger.log(Level.SEVERE, "Failed to load or create $fileName...", exception)
        }

        return this
    }

    /**
     * Saves a [YamlConfiguration].
     *
     * @param fileName the name of the [YamlConfiguration] to save
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun saveFile(fileName: String): FileManager {
        if (fileName.isEmpty()) return this

        val configuration = getFile(fileName) ?: return this

        try {
            configuration.save(File(this.dataFolder, fileName))
        } catch (exception: Exception) {
            this.logger.log(Level.SEVERE, "Failed to save: $fileName...", exception)
        }

        return this
    }

    /**
     * Gets a [YamlConfiguration].
     *
     * @param fileName the name of the [YamlConfiguration] to save
     * @return [YamlConfiguration]
     * @since 1.8.4
     */
    public fun getFile(fileName: String): YamlConfiguration? {
        if (fileName.isEmpty()) return null

        var configuration: YamlConfiguration? = null

        for (key in this.files.keys) {
            if (!key.equals(fileName, ignoreCase = true)) continue

            configuration = this.files[key]

            break
        }

        return configuration
    }

    /**
     * Reload all other [YamlConfiguration]'s.
     *
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun reloadFiles(): FileManager {
        this.files.forEach { (key: String, configuration: YamlConfiguration) ->
            try {
                configuration.save(key)
                configuration.load(key)
            } catch (exception: IOException) {
                this.logger.log(Level.SEVERE, "Failed to load: $key...", exception)
            }
        }

        return this
    }

    /**
     * Load files with one level of recursion
     *
     * @param resolvedFolder the [File] to check
     * @param folder the folder to load
     * @since 1.8.4
     */
    private fun loadFiles(resolvedFolder: File, folder: String) {
        val filesList = resolvedFolder.listFiles()

        if (filesList != null) {
            for (directory in filesList) {
                if (directory.isDirectory) {
                    val dir = directory.list()

                    if (dir != null) {
                        for (name in dir) {
                            if (!name.endsWith(".yml")) continue

                            val file = CustomFile(directory).apply(name)

                            if (file != null && file.exists()) {
                                this.customFiles.add(file)

                                if (this.isLogging) this.logger.info("Loaded new custom file: " + folder + "/" + directory.name + "/" + name + ".")
                            }
                        }
                    }
                } else {
                    val name = directory.name

                    if (!name.endsWith(".yml")) continue

                    val file = CustomFile(resolvedFolder).apply(name)

                    if (file != null && file.exists()) {
                        customFiles.add(file)

                        if (this.isLogging) logger.info("Loaded new custom file: $folder/$name.")
                    }
                }
            }
        }
    }

    /**
     * Get a [CustomFile].
     *
     * @param file the [CustomFile] without the file extension
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun getCustomFile(file: String): CustomFile? {
        if (file.isEmpty()) return null

        var customFile: CustomFile? = null

        for (key in this.customFiles) {
            if (!key.strippedName.equals(file, ignoreCase = true)) continue

            customFile = key

            break
        }

        return customFile
    }

    /**
     * Removes a [CustomFile] from the custom files map
     *
     * @param file the file to remove
     */
    public fun removeCustomFile(file: String) {
        val customFile = getCustomFile(file) ?: return

        this.customFiles.remove(customFile)
    }

    /**
     * Adds a [CustomFile].
     *
     * @param file the [CustomFile] to add
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun addCustomFile(file: CustomFile?): FileManager {
        if (file == null) return this

        this.customFiles.add(file)

        return this
    }

    /**
     * Save a [CustomFile].
     *
     * @param key the name of the [CustomFile] to save
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun saveCustomFile(key: String): FileManager {
        if (key.isEmpty()) return this

        val file = getCustomFile(key) ?: return this

        file.save()

        return this
    }

    /**
     * Reload a specific [CustomFile].
     *
     * @param key the name of the [YamlConfiguration] to reload
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun reloadCustomFile(key: String): FileManager {
        if (key.isEmpty()) return this

        val file = getCustomFile(key) ?: return this

        file.reload()

        return this
    }

    /**
     * Adds a folder to the hashset if it doesn't exist.
     *
     * @param folder the folder to add
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun addFolder(folder: String): FileManager {
        if (folder.isEmpty()) return this
        if (this.folders.contains(folder)) return this

        this.folders.add(folder)

        return this
    }

    /**
     * Removes a folder from the hashset if it exists.
     *
     * @param folder the folder to remove
     * @return [FileManager]
     * @since 1.8.4
     */
    public fun removeFolder(folder: String): FileManager {
        if (folder.isEmpty()) return this

        this.folders.remove(folder)

        return this
    }

    /**
     * Gets a set of folders.
     *
     * @return an unmodifiable set of folders
     * @since 1.8.4
     */
    public fun getFolders(): Set<String> {
        return Collections.unmodifiableSet(this.folders)
    }

    /**
     * Gets a set of files.
     *
     * @return an unmodifiable set of files
     * @since 1.8.4
     */
    public fun getCustomFiles(): Set<CustomFile> {
        return Collections.unmodifiableSet(this.customFiles)
    }

    /**
     * Gets a map of files.
     *
     * @return an unmodifiable map of other files
     * @since 1.8.4
     */
    public fun getFiles(): Map<String, YamlConfiguration> {
        return Collections.unmodifiableMap(this.files)
    }
}