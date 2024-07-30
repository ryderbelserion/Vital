package com.ryderbelserion.vital.core.util;

import java.awt.Color;

/**
 * Utilities related to colors.
 *
 * @version 2.3.1
 * @since 1.8
 */
public class ColorUtil {

    /**
     * Empty constructor.
     */
    public ColorUtil() {
        throw new AssertionError();
    }

    /**
     * Converts hex colors to rgb.
     *
     * @param value the hex color
     * @return {@link Color}
     * @since 1.8
     */
    public static Color toColor(final String value) {
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
     * @since 1.8
     */
    public static String toHex(final Color color) {
        return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}