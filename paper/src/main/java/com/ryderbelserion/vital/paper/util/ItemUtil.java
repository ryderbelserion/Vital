package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.paper.enums.Support;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * All utilities related to items and ids.
 *
 * @author Ryder Belserion
 * @version 2.4.8
 * @since 1.0
 */
public class ItemUtil {

    /**
     * Empty constructor
     */
    private ItemUtil() {
        throw new AssertionError();
    }

    private static final Vital api = Vital.api();
    private static final ComponentLogger logger = api.getLogger();
    private static final boolean isLogging = api.isLogging();

    /**
     * Get a {@link Material} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Material} or null if not found
     * @since 1.0
     */
    public static @Nullable Material getMaterial(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.MATERIAL.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid material.", value);

            return null;
        }
    }

    /**
     * Send a {@link String} with {@link PlaceholderAPI} support.
     *
     * @param value the {@link String} to send
     * @param uuid the {@link UUID} of the {@link Player} to send the {@link String} to
     * @return {@link String}
     * @since 1.0
     */
    public static @NotNull String color(@NotNull final String value, @Nullable final UUID uuid) {
        if (value.isEmpty()) return "";

        return uuid != null ? color(value, null, Bukkit.getPlayer(uuid)) : color(value, null, null);
    }

    /**
     * Parse placeholders in a {@link String} than return a {@link String}.
     *
     * @param value        the {@link String} to alter
     * @param placeholders the {@link Map} to use
     * @param player       the {@link Player} to send the message to
     * @return {@link String}
     * @since 1.0
     */
    public static String color(@NotNull final String value, @Nullable Map<String, String> placeholders, @Nullable final Player player) {
        if (value.isEmpty()) return "";

        String clonedMessage = Support.placeholder_api.isEnabled() && player != null ? PlaceholderAPI.setPlaceholders(player, value) : value;

        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey().toLowerCase();
                String entryValue = entry.getValue();

                clonedMessage = clonedMessage.replace(key, entryValue);
            }
        }

        return color(clonedMessage);
    }

    /**
     * Colors a message using legacy color codes.
     *
     * @param message the message
     * @return the built string
     */
    public static String color(@NotNull final String message) {
        Matcher matcher = Pattern.compile("#([A-Fa-f0-9]{6})").matcher(message);
        StringBuilder buffer = new StringBuilder();

        while (matcher.find()) {
            matcher.appendReplacement(buffer, net.md_5.bungee.api.ChatColor.of(matcher.group()).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    /**
     * Get a {@link Sound} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Sound} or null if not found
     * @since 1.0
     */
    public static @Nullable Sound getSound(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.SOUNDS.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid sound.", value);

            return null;
        }
    }

    /**
     * Get an {@link Enchantment} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Enchantment} or null if not found
     * @since 1.0
     */
    public static @Nullable Enchantment getEnchantment(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid enchantment.", value);

            return null;
        }
    }

    /**
     * Get a {@link TrimPattern} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link TrimPattern} or null if not found
     * @since 1.0
     */
    public static @Nullable TrimPattern getTrimPattern(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid trim pattern.", value);

            return null;
        }
    }

    /**
     * Get a {@link TrimMaterial} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link TrimMaterial} or null if not found
     * @since 1.0
     */
    public static @Nullable TrimMaterial getTrimMaterial(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid trim material.", value);

            return null;
        }
    }

    /**
     * Get a {@link PotionType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link PotionType} or null if not found
     * @since 1.0
     */
    public static @Nullable PotionType getPotionType(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.POTION.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid potion type.", value);

            return null;
        }
    }

    /**
     * Get a {@link PotionEffectType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link PotionEffectType} or null if not found
     * @since 1.0
     */
    public static @Nullable PotionEffectType getPotionEffect(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.POTION_EFFECT_TYPE.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid potion effect type.", value);

            return null;
        }
    }

    /**
     * Get a {@link Particle} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Particle} or null if not found
     * @since 1.0
     */
    public static @Nullable Particle getParticleType(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.PARTICLE_TYPE.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid particle type.", value);

            return null;
        }
    }

    /**
     * Get a {@link PatternType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link PatternType} or null if not found
     * @since 1.0
     */
    public static @Nullable PatternType getPatternType(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.BANNER_PATTERN.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid banner type.", value);

            return null;
        }
    }

    /**
     * Gets an {@link EntityType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link EntityType} or null if not found
     * @since 1.0
     */
    public static @Nullable EntityType getEntity(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.ENTITY_TYPE.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid entity type.", value);

            return null;
        }
    }

    /**
     * Get an {@link Attribute} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Attribute} or null if not found
     * @since 1.0
     */
    public static @Nullable Attribute getAttribute(@NotNull final String value) {
        if (value.isEmpty()) {
            if (isLogging) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.ATTRIBUTE.get(getKey(value));
        } catch (Exception exception) {
            if (isLogging) logger.error("{} is an invalid attribute.", value);

            return null;
        }
    }

    /**
     * Check the {@link NamespacedKey}.
     *
     * @param value the {@link String} to check
     * @return {@link NamespacedKey}
     * @since 1.0
     */
    private static @NotNull NamespacedKey getKey(@NotNull final String value) {
        return NamespacedKey.minecraft(value);
    }

    /**
     * Encodes and serializes an {@link ItemStack} into a {@link String}.
     *
     * @param itemStack {@link ItemStack}
     * @return {@link String}
     * @since 1.6
     */
    public static String toBase64(@NotNull final ItemStack itemStack) {
        return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }

    /**
     * Deserialize a base64 {@link String} back into an {@link ItemStack}.
     *
     * @param base64 the base64 {@link String}
     * @return {@link ItemStack}
     * @since 1.6
     */
    public static @NotNull ItemStack fromBase64(@NotNull final String base64) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
    }
}