package com.ryderbelserion.vital.core.util;

import com.ryderbelserion.vital.core.Vital;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A class containing utilities to extract or obtain files from directories.
 *
 * @author Ryder Belserion
 * @version 1.6
 * @since 1.0
 */
public class FileUtil {

    private FileUtil() {
        throw new AssertionError();
    }

    private static @NotNull final Vital api = Vital.api();
    private static @NotNull final Logger logger = api.getLogger();

    /**
     * Extracts a single file from a directory in the jar.
     *
     * @param object the class object to get the {@link InputStream} from
     * @param fileName the name of the file
     * @param output the {@link Path} to output to
     * @param overwrite whether to overwrite the folder or not
     * @since 1.0
     */
    public static void extract(@Nullable final Class<?> object, @NotNull final String fileName, @Nullable final Path output, final boolean overwrite) {
        if (output == null || object == null || fileName.isEmpty()) return;

        try (InputStream stream = object.getResourceAsStream("/" + fileName)) {
            if (stream == null) {
                throw new RuntimeException("Could not read file from jar! (" + fileName + ")");
            }

            Path path = output.resolve(fileName);

            if (!Files.exists(path) || overwrite) {
                Files.createDirectories(path.getParent());
                Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception exception) {
            logger.severe("Failed to extract " + fileName + " from the jar.");
        }
    }

    /**
     * Allows you to read the resources at a given {@link Path} within the jar file.
     *
     * @param consumer the consumer to read the resources
     * @param input the input folder
     * @throws IOException throws an exception if failed
     */
    public static void visit(final Consumer<Path> consumer, final String input) throws IOException {
        final URL resource = FileUtil.class.getClassLoader().getResource("config.yml");

        if (resource == null) {
            throw new IllegalStateException("We are lacking awareness of the files in src/main/resources/" + input);
        }

        final URI path = URI.create(resource.toString().split("!")[0] + "!/");

        try (final FileSystem fileSystem = FileSystems.newFileSystem(path, Map.of("create", "true"))) {
            final Path toVisit = fileSystem.getPath(input);

            if (Files.exists(toVisit)) {
                consumer.accept(toVisit);
            }
        }
    }

    /**
     * Extracts files from the jar.
     *
     * @param input the folder to write to
     * @param message the message to send
     */
    public static void extract(String input, String message) {
        extract(input, input, message, false);
    }

    /**
     * Extracts files from the jar.
     *
     * @param input the folder to read from
     * @param output the folder to write to
     * @param message the message to send
     * @param overwrite delete the existing output folder if true
     */
    public static void extract(String input, String output, String message, boolean overwrite) {
        try {
            visit(path -> {
                if (api.isLogging()) logger.info(message);

                final Path directory = api.getDirectory().toPath().resolve(output);

                try {
                    // If true, we delete the directory and then instantly re-create!
                    if (overwrite) {
                        Files.deleteIfExists(directory);
                    }

                    if (!Files.exists(directory)) {
                        Files.createDirectory(directory);

                        try (final Stream<Path> files = Files.walk(path)) {
                            files.filter(Files::isRegularFile).forEach(file -> {
                                try {
                                    final Path langFile = directory.resolve(file.getFileName().toString());

                                    if (!Files.exists(langFile)) {
                                        try (final InputStream stream = Files.newInputStream(file)) {
                                            Files.copy(stream, langFile);
                                        }
                                    }
                                } catch (IOException exception) {
                                    logger.log(Level.SEVERE, "Encountered an I/O error whilst trying to load the file: " + file.getFileName().toString(), exception);
                                }
                            });
                        }
                    }
                } catch (IOException exception) {
                    logger.log(Level.SEVERE, "Encountered an I/O error whilst trying to load the directory: " + directory, exception);
                }
            }, input);
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Encountered an I/O error whilst loading files.", exception);
        }
    }

    /**
     * Extracts multiple files from a directory in the jar.
     *
     * @param object the class object to get the {@link InputStream} from
     * @param sourceDir the source directory
     * @param outDir the {@link Path} to output to
     * @param replace whether to overwrite the folder or not
     * @since 1.0
     */
    public static void extracts(@Nullable final Class<?> object, @NotNull String sourceDir, @Nullable final Path outDir, final boolean replace) {
        if (object == null || outDir == null || sourceDir.isEmpty()) return;

        try (JarFile jarFile = new JarFile(Path.of(object.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final String path = sourceDir.substring(1);
            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                final String name = entry.getName();

                if (!name.startsWith(path)) continue;

                final Path file = outDir.resolve(name.substring(path.length()));

                final boolean exists = Files.exists(file);

                if (!replace && exists) continue;

                if (entry.isDirectory()) {
                    if (!exists) {
                        try {
                            Files.createDirectories(file);
                        } catch (IOException exception) {
                            logger.severe(name + " could not be created");
                        }
                    }

                    continue;
                }

                try (
                        final InputStream inputStream = new BufferedInputStream(jarFile.getInputStream(entry));
                        final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file.toFile()))
                ) {
                    final byte[] buffer = new byte[4096];
                    int readCount;

                    while ((readCount = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, readCount);
                    }

                    outputStream.flush();
                } catch (IOException exception) {
                    logger.severe("Failed to extract (" + name + ") from jar!");
                }
            }
        } catch (IOException exception) {
            logger.severe("Failed to extract file (" + sourceDir + ") from jar!");
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension
     *
     * @param directory the folder to check
     * @param extension the file extension
     * @param removeExtension removes the file extension if true
     * @return a {@link List<String>} of files that meet the criteria
     * @since 1.0
     */
    public static List<String> getFiles(@NotNull final Path directory, @NotNull final String extension, boolean removeExtension) {
        final List<String> files = new ArrayList<>();

        final File[] iterator = directory.toFile().listFiles();

        if (iterator == null) return files;

        for (final File file : iterator) {
            if (file.isDirectory()) {
                final File[] recursive = directory.resolve(file.getName()).toFile().listFiles();

                if (recursive != null) {
                    for (File key : recursive) {
                        final String name = key.getName();

                        if (!name.endsWith(extension)) continue;

                        files.add(removeExtension ? name.replace(extension, "") : file.getName() + File.separator + name);
                    }
                }
            } else {
                final String name = file.getName();

                if (!name.endsWith(extension)) continue;

                files.add(removeExtension ? name.replace(extension, "") : name);
            }
        }

        return files;
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension
     *
     * @param directory the directory to check
     * @param folder the fallback folder
     * @param extension the file extension
     * @return a {@link List<String>} of files that meet the criteria
     * @since 1.0
     */
    public static List<String> getFiles(@NotNull final Path directory, @NotNull final String folder, @NotNull String extension) {
        return getFiles(folder.isEmpty() ? directory : directory.resolve(folder), extension, true);
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension
     *
     * @param directory the directory to check
     * @param folder the fallback folder
     * @param extension the file extension
     * @return a {@link List<String>} of files that meet the criteria
     * @since 1.5
     */
    public static List<File> getFileObjects(@NotNull final Path directory, @NotNull final String folder, @NotNull String extension) {
        List<File> files = new ArrayList<>();

        getFiles(folder.isEmpty() ? directory : directory.resolve(folder), extension, false).forEach(file -> files.add(directory.resolve(folder).resolve(file).toFile()));

        return files;
    }
}