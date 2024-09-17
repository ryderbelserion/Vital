package com.ryderbelserion.vital.paper.util;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * All utilities related to dye colors.
 *
 * @author ryderbelserion
 * @version 0.0.2
 * @since 0.0.1
 */
public class DyeUtil {

    /**
     * Empty constructor
     *
     * @since 0.0.1
     */
    private DyeUtil() {
        throw new AssertionError();
    }

    /**
     * Gets the dye color from a {@link String}.
     *
     * @param value the value to check
     * @return {@link DyeColor}
     * @since 0.0.1
     */
    public static @NotNull DyeColor getDyeColor(@NotNull final String value) {
        if (value.isEmpty()) return DyeColor.WHITE;

        return switch (value.toUpperCase()) {
            case "ORANGE" -> DyeColor.ORANGE;
            case "MAGENTA" -> DyeColor.MAGENTA;
            case "LIGHT_BLUE" -> DyeColor.LIGHT_BLUE;
            case "YELLOW" -> DyeColor.YELLOW;
            case "LIME" -> DyeColor.LIME;
            case "PINK" -> DyeColor.PINK;
            case "GRAY" -> DyeColor.GRAY;
            case "LIGHT_GRAY" -> DyeColor.LIGHT_GRAY;
            case "CYAN" -> DyeColor.CYAN;
            case "PURPLE" -> DyeColor.PURPLE;
            case "BLUE" -> DyeColor.BLUE;
            case "BROWN" -> DyeColor.BROWN;
            case "GREEN" -> DyeColor.GREEN;
            case "RED" -> DyeColor.RED;
            case "BLACK" -> DyeColor.BLACK;
            default -> DyeColor.WHITE;
        };
    }

    /**
     * Get the {@link Color} from a {@link String}.
     *
     * @param color the {@link String} to check
     * @return {@link Color}
     * @since 0.0.1
     */
    public static @NotNull Color getDefaultColor(@NotNull final String color) {
        if (color.isEmpty()) return Color.WHITE;

        return switch (color.toUpperCase()) {
            case "AQUA" -> Color.AQUA;
            case "BLACK" -> Color.BLACK;
            case "BLUE" -> Color.BLUE;
            case "FUCHSIA" -> Color.FUCHSIA;
            case "GRAY" -> Color.GRAY;
            case "GREEN" -> Color.GREEN;
            case "LIME" -> Color.LIME;
            case "MAROON" -> Color.MAROON;
            case "NAVY" -> Color.NAVY;
            case "OLIVE" -> Color.OLIVE;
            case "ORANGE" -> Color.ORANGE;
            case "PURPLE" -> Color.PURPLE;
            case "RED" -> Color.RED;
            case "SILVER" -> Color.SILVER;
            case "TEAL" -> Color.TEAL;
            case "YELLOW" -> Color.YELLOW;
            default -> Color.WHITE;
        };
    }

    /**
     * Get the {@link Color} from a {@link String} by splitting it and converting it to rgb.
     *
     * @param color the {@link String} to check
     * @return {@link Color}
     * @since 0.0.1
     */
    public static @Nullable Color getColor(@NotNull final String color) {
        if (color.isEmpty()) return null;

        String[] rgb = color.split(",");

        if (rgb.length != 3) {
            return null;
        }

        int red = Integer.parseInt(rgb[0]);
        int green = Integer.parseInt(rgb[1]);
        int blue = Integer.parseInt(rgb[2]);

        return Color.fromRGB(red, green, blue);
    }
}