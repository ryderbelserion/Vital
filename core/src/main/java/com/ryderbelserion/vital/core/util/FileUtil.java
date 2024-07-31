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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * A class containing utilities to extract or obtain files from directories.
 *
 * @author Ryder Belserion
 * @version 2.4.3
 * @since 1.0
 */
public class FileUtil {

    private FileUtil() {
        throw new AssertionError();
    }

    private static @NotNull final Vital api = Vital.api();

    /**
     * Extracts a single file from the src/main/resources
     *
     * @param fileName the name of the file
     * @param overwrite whether to overwrite the folder or not
     * @since 1.0
     */
    public static void extract(@NotNull final String fileName, final boolean overwrite) {
        api.saveResource(fileName, overwrite);
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
     */
    public static void extract(String input) {
        extract(input, input, false);
    }

    /**
     * Extracts files from the jar.
     *
     * @param input the folder to read from
     * @param output the folder to write to
     * @param replaceExisting delete the existing output folder if true
     */
    public static void extract(String input, String output, boolean replaceExisting) {
        try {
            visit(path -> {
                final Path directory = api.getDirectory().toPath().resolve(output);

                try {
                    // If true, we delete the directory and then instantly re-create!
                    if (replaceExisting) {
                        directory.toFile().delete();
                    }

                    if (!Files.exists(directory)) {
                        directory.toFile().mkdirs();

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
                                    exception.printStackTrace();
                                }
                            });
                        }
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }, input);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Extracts multiple files from a directory in the jar.
     *
     * @param object the class object to get the {@link InputStream} from
     * @param sourceDir the source directory
     * @param outDir the {@link Path} to output to
     * @param replaceExisting whether to overwrite the folder or not
     * @since 1.0
     */
    public static void extracts(@Nullable final Class<?> object, @NotNull String sourceDir, @Nullable final Path outDir, final boolean replaceExisting) {
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

                if (!replaceExisting && exists) continue;

                if (entry.isDirectory()) {
                    if (!exists) {
                        try {
                            Files.createDirectories(file);
                        } catch (IOException exception) {
                            exception.printStackTrace();
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
                    exception.printStackTrace();
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension
     *
     * @param dir the folder to check
     * @param extension the file extension
     * @return a {@link List<String>} of files that meet the criteria
     * @since 1.0
     */
    public static List<String> getFiles(@NotNull final File dir, @NotNull final String extension) {
        List<String> files = new ArrayList<>();

        String[] file = dir.list();

        if (file != null) {
            File[] filesList = dir.listFiles();

            if (filesList != null) {
                for (File directory : filesList) {
                    if (directory.isDirectory()) {
                        String[] folder = directory.list();

                        if (folder != null) {
                            for (String name : folder) {
                                if (!name.endsWith(extension)) continue;

                                files.add(name.replaceAll(extension, ""));
                            }
                        }
                    }
                }
            }

            for (String name : file) {
                if (!name.endsWith(extension)) continue;

                files.add(name.replaceAll(extension, ""));
            }
        }

        return Collections.unmodifiableList(files);
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
    public static List<String> getFiles(@NotNull final File directory, @NotNull final String folder, @NotNull String extension) {
        return getFiles(folder.isEmpty() ? directory : new File(directory, folder), extension);
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension
     *
     * @param directory the directory to check
     * @param folder the fallback folder
     * @param extension the file extension
     *
     * @return a {@link List<String>} of files that meet the criteria
     * @since 1.5
     */
    public static List<File> getFileObjects(@NotNull final File directory, @NotNull final String folder, @NotNull String extension) {
        final List<File> files = new ArrayList<>();

        final File root = new File(directory, folder);

        getFiles(folder.isEmpty() ? directory : root, extension).forEach(file -> files.add(new File(root, file)));

        return files;
    }

    private static void grab(final InputStream input, final File output) throws Exception {
        try (InputStream inputStream = input; FileOutputStream outputStream = new FileOutputStream(output)) {
            byte[] buf = new byte[1024];
            int i;

            while ((i = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, i);
            }
        }
    }
}