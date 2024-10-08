package com.ryderbelserion.vital.common.util;

import org.jetbrains.annotations.NotNull;
import java.awt.Color;

/**
 * Utilities related to colors.
 *
 * @version 0.0.4
 * @since 0.0.1
 */
public class ColorUtil {

    /**
     * Utilities related to colors.
     * @since 0.0.1
     */
    public ColorUtil() {
        throw new AssertionError();
    }

    /**
     * Converts hex colors to rgb.
     *
     * @param value the hex color
     * @return {@link Color}
     * @since 0.0.1
     */
    public static Color toColor(@NotNull final String value) {
        return new Color(
                Integer.valueOf(value.substring(1, 3), 16),
                Integer.valueOf(value.substring(3, 5), 16),
                Integer.valueOf(value.substring(5, 7), 16)
        );
    }

    /**
     * Converts color to hex string.
     *
     * @param color {@link Color}
     * @return the completed color
     * @since 0.0.1
     */
    public static String toHex(@NotNull final Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}