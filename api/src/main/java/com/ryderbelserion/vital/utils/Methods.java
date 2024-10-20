package com.ryderbelserion.vital.utils;

import org.jetbrains.annotations.NotNull;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Methods {

    public static String formatInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static String formatDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String convertList(@NotNull final List<String> list) {
        if (list.isEmpty()) return "";

        StringBuilder message = new StringBuilder();

        for (String line : list) {
            message.append(line).append("\n");
        }

        return chomp(message.toString());
    }

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

    public static Optional<Number> tryParseInt(@NotNull final String value) {
        try {
            return Optional.of(Integer.parseInt(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static Optional<Boolean> tryParseBoolean(@NotNull final String value) {
        try {
            return Optional.of(Boolean.parseBoolean(value));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    public static String getEnchant(@NotNull final String enchant) {
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