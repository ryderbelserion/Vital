package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.core.AbstractPlugin;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.logging.Logger;

/**
 * All utilities related to dye colors
 *
 * @author Ryder Belserion
 * @version 1.2
 * @since 1.0
 */
public class DyeUtil {

    private static final Logger logger = AbstractPlugin.api().getLogger();

    /**
     * Gets the dye color from a {@link String}.
     *
     * @param value the value to check
     * @return {@link DyeColor}
     * @since 1.0
     */
    public static @Nullable DyeColor getDyeColor(@NotNull final String value) {
        if (value.isEmpty()) return null;

        Color color = getColor(value);

        if (color == null) {
            logger.severe(value + " is not a valid color.");

            return null;
        }

        return DyeColor.getByColor(color);
    }

    /**
     * Get the {@link Color} from a {@link String}
     *
     * @param color the {@link String} to check
     * @return {@link Color}
     * @since 1.0
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
     * Get the {@link Color} from a {@link String} by splitting it and converting it to rgb
     *
     * @param color the {@link String} to check
     * @return {@link Color}
     * @since 1.0
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