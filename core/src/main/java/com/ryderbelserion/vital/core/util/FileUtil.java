package com.ryderbelserion.vital.core.util;

import com.ryderbelserion.vital.core.AbstractPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

/**
 * A class containing utilities to extract or obtain files from directories.
 *
 * @author Ryder Belserion
 * @version 1.4
 * @since 1.0
 */
public class FileUtil {

    private static @NotNull final AbstractPlugin api = AbstractPlugin.api();
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
}