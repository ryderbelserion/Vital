package com.ryderbelserion.vital.common.utils;

import org.jetbrains.annotations.NotNull;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A class containing utilities to convert or chomp strings
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class StringUtil {

    /**
     * A class containing utilities to convert or chomp strings
     *
     * @author ryderbelserion
     * @since 0.0.1
     */
    private StringUtil() {
        throw new AssertionError();
    }

    /**
     * Converts a {@link Integer} to a {@link String}.
     *
     * @param number number to convert
     * @return {@link String}
     * @since 0.0.1
     */
    public static String formatInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    /**
     * Converts a {@link Double} to a {@link String}.
     *
     * @param number number to convert
     * @return {@link String}
     * @since 0.0.1
     */
    public static String formatDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    /**
     * Loops through a {@link List<String>} and returns a {@link String}.
     *
     * @param list the {@link List<String>} to convert
     * @return {@link String}
     * @since 0.0.1
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
     * Chomps the end of a {@link String} by removing \n or \r.
     * This was taken from Apache StringUtils.
     *
     * @param key the {@link String} to check
     * @return {@link String}
     * @since 0.0.1
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
     * Converts old spigot mapped ids to mojang mapped ids.
     *
     * @param enchant the enchantment to convert
     * @return the mojang mapped id
     * @since 0.0.1
     */
    public static String getEnchant(String enchant) {
        if (enchant.isEmpty()) return "";

        return switch (enchant.toLowerCase()) {
            case "protection_environmental" -> "protection";
            case "protection_fire" -> "fire_protection";
            case "protection_fall" -> "feather_falling";
            case "protection_explosions" -> "blast_protection";
            case "protection_projectile" -> "projectile_protection";
            case "oxygen" -> "respiration";
            case "water_worker" -> "aqua_affinity";
            case "damage_all" -> "sharpness";
            case "damage_undead" -> "smite";
            case "damage_arthropods" -> "bane_of_arthropods";
            case "loot_bonus_mobs" -> "looting";
            case "dig_speed" -> "efficiency";
            case "durability" -> "unbreaking";
            case "loot_bonus_blocks" -> "fortune";
            case "arrow_damage" -> "power";
            case "arrow_knockback" -> "punch";
            case "arrow_fire" -> "flame";
            case "arrow_infinite" -> "infinity";
            case "luck" -> "luck_of_the_sea";

            default -> enchant.toLowerCase();
        };
    }
}