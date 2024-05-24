package com.ryderbelserion.vital.core.util;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * A class containing utilities to convert or chomp strings
 *
 * @author Ryder Belserion
 * @version 1.4
 * @since 1.0
 */
public class StringUtil {

    /**
     * Loops through a {@link List<String>} and returns a {@link String}
     *
     * @param list the {@link List<String>} to convert
     * @return {@link String}
     * @since 1.0
     */
    public static String convertList(@NotNull final List<String> list) {
        if (list.isEmpty()) return "";

        StringBuilder message = new StringBuilder();

        for (String line : list) {
            message.append(line).append("\n");
        }

        return chomp(message.toString());
    }

    /**
     * Chomps the end of a {@link String} by removing \n or \r
     * This was taken from Apache StringUtils
     *
     * @param key the {@link String} to check
     * @return {@link String}
     * @since 1.0
     */
    public static @NotNull String chomp(@NotNull final String key) {
        if (key.isEmpty()) {
            return key;
        }

        char CR = '\r';
        char LF = '\n';

        if (key.length() == 1) {
            char ch = key.charAt(0);

            if (ch == CR || ch == LF) {
                return "";
            }

            return key;
        }

        int lastIdx = key.length() - 1;
        char last = key.charAt(lastIdx);

        if (last == LF) {
            if (key.charAt(lastIdx - 1) == CR) {
                lastIdx--;
            }
        } else if (last != CR) {
            lastIdx++;
        }

        return key.substring(0, lastIdx);
    }

    /**
     * Tries to parse a {@link String} into an {@link Integer}.
     *
     * @param value the {@link String} to parse
     * @return an optional value of the parsed {@link Optional<Integer>} or empty if the {@link String} is not an {@link Integer}
     * @since 1.0
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
     * @since 1.0
     */
    public static Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }
}