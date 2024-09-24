package com.ryderbelserion.vital.common.utils;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.common.api.Provider;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A class containing utilities to extract or obtain files from directories.
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public class FileUtil {

    /**
     * A class containing utilities to extract or obtain files from directories.
     *
     * @author ryderbelserion
     * @since 0.0.1
     */
    private FileUtil() {
        throw new AssertionError();
    }

    private static @NotNull final VitalAPI api = Provider.getApi();
    private static @NotNull final ComponentLogger logger = api.getComponentLogger();
    private static @NotNull final File dataFolder = api.getDirectory();

    /**
     * Downloads anything at runtime into any directory.
     *
     * @param link {@link String}
     * @param directory {@link File}
     * @since 0.0.1
     */
    public static void download(final String link, final File directory) {
        CompletableFuture.runAsync(() -> {
            URL url = null;

            try {
                url = URI.create(link).toURL();
            } catch (MalformedURLException exception) {
                exception.printStackTrace();
            }

            if (url == null) return;

            try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream()); FileOutputStream outputStream = new FileOutputStream(directory)) {
                outputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Writes to a file.
     *
     * @param file the {@link File}
     * @param format the format to write
     */
    public static void write(final File file, final String format) {
        try (final FileWriter writer = new FileWriter(file, true); final BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(format);
            bufferedWriter.newLine();
            writer.flush();
        } catch (Exception exception) {
            logger.warn("Failed to write to: {}", file.getName(), exception);
        }
    }

    /**
     * Zip a file into a .gz file.
     *
     * @param file the {@link File}
     * @param purge true or false
     */
    public static void zip(final File file, final boolean purge) {
        zip(List.of(file), null, "", purge);
    }

    /**
     * Zip multiple files into a .gz file.
     *
     * @param file the directory to zip
     * @param extension the file extension
     * @param purge true or false
     */
    public static void zip(final File file, final String extension, final boolean purge) {
        final List<File> files = FileUtil.getFileObjects(dataFolder, file.getName(), extension);

        if (files.isEmpty()) return;

        boolean hasNonEmptyFile = false;

        for (final File zip : files) {
            if (zip.exists() && zip.length() > 0) {
                hasNonEmptyFile = true;

                break;
            }
        }

        if (!hasNonEmptyFile) {
            if (api.isVerbose()) {
                logger.warn("All log files are empty. No zip file will be created.");
            }

            return;
        }

        int count = FileUtil.getFiles(file, ".gz", true).size();

        count++;

        zip(files, file, "-" + count, purge);
    }

    /**
     * Zip files into a .gz file.
     *
     * @param files the list of files
     * @param directory the directory
     * @param extra anything extra to add to the file
     * @param purge true or false
     */
    public static void zip(final List<File> files, @Nullable final File directory, final String extra, final boolean purge) {
        if (files.isEmpty()) return;

        final StringBuilder builder = new StringBuilder();

        builder.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        if (!builder.isEmpty()) {
            builder.append(extra);
        }

        builder.append(".gz");

        if (directory != null) {
            directory.mkdirs();
        }

        final File zipFile = new File(directory == null ? dataFolder : directory, builder.toString());

        try (final FileOutputStream fileOutputStream = new FileOutputStream(zipFile); ZipOutputStream zipOut = new ZipOutputStream(fileOutputStream)) {
            for (File file : files) {
                if (file.length() > 0) {
                    try (final FileInputStream fileInputStream = new FileInputStream(file)) {
                        final ZipEntry zipEntry = new ZipEntry(file.getName());

                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;

                        while ((length = fileInputStream.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                    }

                    if (purge) file.delete();
                } else {
                    if (api.isVerbose()) {
                        logger.warn("The file named {}'s size is 0, We are not adding to zip.", file.getName());
                    }
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Extracts a single file from the src/main/resources.
     *
     * @param fileName the name of the file
     * @param overwrite whether to overwrite the folder or not
     * @since 0.0.1
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
     * @since 0.0.1
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
     * @since 0.0.1
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
     * @since 0.0.1
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
     * @since 0.0.1
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
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension.
     *
     * @param dir the folder to check
     * @param extension the file extension
     * @param keepExtension true or false
     * @return a {@link List<String>} of files that meet the criteria
     * @since 0.0.1
     */
    public static List<String> getFiles(@NotNull final File dir, @NotNull final String extension, final boolean keepExtension) {
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

                                files.add(keepExtension ? name : name.replaceAll(extension, ""));
                            }
                        }
                    }
                }
            }

            for (String name : file) {
                if (!name.endsWith(extension)) continue;

                files.add(keepExtension ? name : name.replaceAll(extension, ""));
            }
        }

        return Collections.unmodifiableList(files);
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension.
     *
     * @param directory the directory to check
     * @param folder the fallback folder
     * @param extension the file extension
     * @return a {@link List<String>} of files that meet the criteria
     * @since 0.0.1
     */
    public static List<String> getFiles(@NotNull final File directory, @NotNull final String folder, @NotNull String extension) {
        return getFiles(folder.isEmpty() ? directory : new File(directory, folder), extension, false);
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension.
     *
     * @param directory the directory to check
     * @param folder the fallback folder
     * @param extension the file extension
     *
     * @return a {@link List<String>} of files that meet the criteria
     * @since 0.0.1
     */
    public static List<File> getFileObjects(@NotNull final File directory, @NotNull final String folder, @NotNull String extension) {
        final List<File> files = new ArrayList<>();

        final File root = new File(directory, folder);

        getFiles(folder.isEmpty() ? directory : root, extension, true).forEach(file -> files.add(new File(root, file)));

        return files;
    }
}