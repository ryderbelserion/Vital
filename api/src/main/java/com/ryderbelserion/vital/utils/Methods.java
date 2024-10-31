package com.ryderbelserion.vital.utils;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
import java.math.RoundingMode;
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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A methods class
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 2.0.0
 */
public class Methods {

    private static final Vital instance = VitalProvider.get();

    private static final ComponentLogger logger = instance.getLogger();

    /**
     * A methods class
     *
     * @author ryderbelserion
     * @since 0.0.4
     */
    public Methods() {
        throw new AssertionError();
    }

    /**
     * Turns an integer into a string.
     *
     * @param number the number
     * @return the string
     * @since 0.0.4
     */
    public static String fromInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    /**
     * Turns a double into a string.
     *
     * @param number the number
     * @return the string
     * @since 0.0.4
     */
    public static String fromDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    /**
     * Converts a list to a string.
     *
     * @param list the list
     * @return the string
     * @since 0.0.1
     */
    public static String toString(final List<String> list) {
        if (list.isEmpty()) return "";

        StringBuilder message = new StringBuilder();

        for (String line : list) {
            message.append(line).append("\n");
        }

        return message.toString();
    }

    /**
     * Parses a message.
     *
     * @param message the {@link String} to alter
     * @return {@link Component}
     * @since 0.0.1
     */
    public static @NotNull Component parse(@NotNull final String message) {
        if (message.isEmpty()) return Component.empty();

        return MiniMessage.miniMessage().deserialize(message).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    /**
     * Converts a {@link String} to {@link Component}.
     *
     * @param component {@link String}
     * @return {@link Component}
     * @since 0.0.3
     */
    public static @NotNull Component toComponent(@NotNull final String component) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(component.replace("§", "&"));
    }

    /**
     * Converts a lore to minimessage strings
     *
     * @param lore {@link List<Component>}
     * @return {@link List<Component>}
     * @since 0.0.3
     */
    public static @NotNull List<Component> toComponent(@NotNull final List<String> lore) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(toComponent(line)));
        }};
    }

    /**
     * Converts a lore to minimessage strings
     *
     * @param lore {@link List<Component>}
     * @return {@link List<Component>}
     * @since 0.0.3
     */
    public static @NotNull List<String> fromComponent(@NotNull final List<Component> lore) {
        return fromComponent(lore, false);
    }

    /**
     * Converts a lore to minimessage strings
     *
     * @param lore {@link List<Component>}
     * @param isMessage true or false
     * @return {@link List<Component>}
     * @since 0.0.3
     */
    public static @NotNull List<String> fromComponent(@NotNull final List<Component> lore, final boolean isMessage) {
        return new ArrayList<>(lore.size()) {{
            lore.forEach(line -> add(fromComponent(line, isMessage)));
        }};
    }

    /**
     * Converts a {@link Component} to {@link String}.
     *
     * @param component {@link Component}
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String fromComponent(@NotNull final Component component) {
        return fromComponent(component, false);
    }

    /**
     * Converts a {@link Component} to {@link String}.
     *
     * @param component {@link Component}
     * @param isMessage true or false
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String fromComponent(@NotNull final Component component, final boolean isMessage) {
        final String value = MiniMessage.miniMessage().serialize(component);

        if (isMessage) {
            return value.replace("\\<", "<");
        }

        return value;
    }

    /**
     * Converts a {@link String} to a {@link Component} then a {@link String}.
     *
     * @param component {@link String}
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String convert(@NotNull final String component)  {
        return convert(component, false);
    }

    /**
     * Converts a {@link List<String>} to a {@link List<Component>} then a {@link List<String>}.
     *
     * @param components {@link List<String>}
     * @return {@link List<String>}
     * @since 0.0.3
     */
    public static @NotNull List<String> convert(@NotNull final List<String> components) {
        return convert(components, false);
    }

    /**
     * Converts a {@link List<String>} to a {@link List<Component>} then a {@link List<String>}.
     *
     * @param components {@link List<String>}
     * @param isMessage true or false
     * @return {@link List<String>}
     * @since 0.0.3
     */
    public static @NotNull List<String> convert(@NotNull final List<String> components, final boolean isMessage) {
        return new ArrayList<>(components.size()) {{
            components.forEach(line -> add(convert(line, isMessage)));
        }};
    }

    /**
     * Converts a {@link String} to a {@link Component} then a {@link String}.
     *
     * @param component {@link String}
     * @param isMessage true or false
     * @return {@link String}
     * @since 0.0.3
     */
    public static @NotNull String convert(@NotNull final String component, final boolean isMessage)  {
        return fromComponent(toComponent(component), isMessage);
    }

    /**
     * Downloads anything at runtime into any directory.
     *
     * @param link {@link String}
     * @param directory {@link File}
     * @since 0.0.1
     */
    public static void download(@NotNull final String link, @NotNull final File directory) {
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
     * @param input the {@link File}
     * @param format the format to write
     */
    public static void write(@NotNull final File input, @NotNull final String format) {
        try (final FileWriter writer = new FileWriter(input, true); final BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(format);
            bufferedWriter.newLine();

            writer.flush();
        } catch (Exception exception) {
            logger.warn("Failed to write to: {}", input.getName(), exception);
        }
    }

    /**
     * Zip a file into a .gz file.
     *
     * @param input the {@link File}
     * @param purge true or false
     * @since 0.0.1
     */
    public static void zip(@NotNull final File input, final boolean purge) {
        zip(List.of(input), null, "", purge);
    }

    /**
     * Zip multiple files into a .gz file.
     *
     * @param input the directory to zip
     * @param extension the file extension
     * @param purge true or false
     * @since 0.0.1
     */
    public static void zip(@NotNull final File input, @NotNull final String extension, final boolean purge) {
        final List<File> files = getFiles(instance.getDataFolder(), input.getName(), extension, false);

        if (files.isEmpty()) return;

        boolean hasNonEmptyFile = false;

        for (final File zip : files) {
            if (zip.exists() && zip.length() > 0) {
                hasNonEmptyFile = true;

                break;
            }
        }

        if (!hasNonEmptyFile) {
            return;
        }

        int count = getFiles(input, ".gz", true).size();

        count++;

        zip(files, input, "-" + count, purge);
    }

    /**
     * Zip files into a .gz file.
     *
     * @param files the list of files
     * @param directory the directory
     * @param extra anything extra to add to the file
     * @param purge true or false
     * @since 0.0.1
     */
    public static void zip(@NotNull final List<File> files, @Nullable final File directory, final String extra, final boolean purge) {
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

        final File zipFile = new File(directory == null ? instance.getDataFolder() : directory, builder.toString());

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
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Extracts a single file from the src/main/resources.
     *
     * @param input the name of the file
     * @param overwrite whether to overwrite the folder or not
     * @since 0.0.1
     */
    public static void extract(@NotNull final String input, final boolean overwrite) {
       instance.saveResource(input, overwrite);
    }

    /**
     * Allows you to read the resources at a given {@link Path} within the jar file.
     *
     * @param consumer the consumer to read the resources
     * @param input the input folder
     * @throws IOException throws an exception if failed
     * @since 0.0.1
     */
    public static void visit(@NotNull final Consumer<Path> consumer, @NotNull final String input) throws IOException {
        final URL resource = Methods.class.getClassLoader().getResource("config.yml");

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
    public static void extract(@NotNull final String input) {
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
    public static void extract(@NotNull final String input, @NotNull final String output, final boolean replaceExisting) {
        try {
            visit(path -> {
                final Path directory = instance.getDataFolder().toPath().resolve(output);

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
     * @param input the source directory
     * @param output the {@link Path} to output to
     * @param replaceExisting whether to overwrite the folder or not
     * @since 0.0.1
     */
    public static void extracts(@Nullable final Class<?> object, @NotNull String input, @Nullable final Path output, final boolean replaceExisting) {
        if (object == null || output == null || input.isEmpty()) return;

        try (JarFile jarFile = new JarFile(Path.of(object.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile())) {
            final String path = input.substring(1);
            final Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                final String name = entry.getName();

                if (!name.startsWith(path)) continue;

                final Path file = output.resolve(name.substring(path.length()));

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

                try (final InputStream inputStream = new BufferedInputStream(jarFile.getInputStream(entry)); final OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file.toFile()))) {
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
     * @param directory the directory to check
     * @param folder the fallback folder
     * @param extension the file extension
     * @param keepExtension keeps the file extension
     *
     * @return a {@link List<String>} of files that meet the criteria
     * @since 0.0.3
     */
    public static List<String> getNames(@NotNull final File directory, @NotNull final String folder, @NotNull final String extension, final boolean keepExtension) {
        return getFiles(directory, folder, extension, keepExtension).stream().map(File::getName).collect(Collectors.toList());
    }

    /**
     * Returns a {@link List<String>} of files in a directory if they end in a specific extension.
     *
     * @param directory the directory to check
     * @param extension the file extension
     * @param keepExtension keeps the file extension
     *
     * @return a {@link List<String>} of files that meet the criteria
     * @since 0.0.3
     */
    public static List<String> getNames(@NotNull final File directory, @NotNull final String extension, final boolean keepExtension) {
        return getFiles(directory, extension, keepExtension).stream().map(File::getName).collect(Collectors.toList());
    }

    /**
     * Returns a {@link List<File>} of files in a directory if they end in a specific extension.
     *
     * @param directory the directory to check
     * @param folder the fallback folder
     * @param extension the file extension
     * @param keepExtension keeps the file extension
     *
     * @return a {@link List<File>} of files that meet the criteria
     * @since 0.0.1
     */
    public static List<File> getFiles(@NotNull final File directory, @NotNull final String folder, @NotNull final String extension, final boolean keepExtension) {
        return getFiles(folder.isEmpty() ? directory : new File(directory, folder), extension, keepExtension);
    }

    /**
     * Returns a {@link List<File>} of files in a directory if they end in a specific extension.
     *
     * @param directory the folder to check
     * @param extension the file extension
     * @param keepExtension true or false
     * @return a {@link List<File>} of files that meet the criteria
     * @since 0.0.1
     */
    public static List<File> getFiles(@NotNull final File directory, @NotNull final String extension, final boolean keepExtension) {
        List<File> files = new ArrayList<>();

        String[] list = directory.list();

        if (list == null) return files;

        File[] array = directory.listFiles();

        if (array == null) return files;

        for (final File file : array) {
            if (file.isDirectory()) {
                final String[] folder = file.list();

                if (folder != null) {
                    for (final String name : folder) {
                        if (!name.endsWith(extension)) continue;

                        files.add(new File(keepExtension ? name : name.replaceAll(extension, "")));
                    }
                }
            } else {
                final String name = file.getName();

                if (!name.endsWith(extension)) continue;

                files.add(new File(keepExtension ? name : name.replaceAll(extension, "")));
            }
        }

        return Collections.unmodifiableList(files);
    }

    /**
     * Tries to parse a {@link String} into an {@link Integer}.
     *
     * @param value the {@link String} to parse
     * @return an optional value of the parsed {@link Optional<Integer>} or empty if the {@link String} is not an {@link Integer}
     * @since 0.0.1
     */
    public static Optional<Number> tryParseInt(@NotNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    /**
     * Tries to parse a {@link String} into a {@link Boolean}.
     *
     * @param value the {@link String} to parse
     * @return an optional value of the parsed {@link Optional<Boolean>} or empty if the {@link String} is not a {@link Boolean}
     * @since 0.0.1
     */
    public static Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    /**
     * Converts a double to a string with rounding and proper formatting.
     *
     * @param value the double to format
     * @return the string
     * @since 0.0.1
     */
    public static String format(final double value) {
        final DecimalFormat decimalFormat = new DecimalFormat(instance.getNumberFormat());

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(value);
    }

    /**
     * Gets the rounding mode from the config
     *
     * @return the rounding mode
     * @since 0.0.1
     */
    public static RoundingMode mode() {
        return RoundingMode.valueOf(instance.getRounding().toUpperCase());
    }
}