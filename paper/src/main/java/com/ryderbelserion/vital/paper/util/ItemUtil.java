package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.core.AbstractPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.logging.Logger;

/**
 * All utilities related to items and ids.
 *
 * @author Ryder Belserion
 * @version 1.5
 * @since 1.0
 */
public class ItemUtil {

    private ItemUtil() {
        throw new AssertionError();
    }

    private static final Logger logger = AbstractPlugin.api().getLogger();

    /**
     * Get a {@link Material} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Material} or null if not found
     * @since 1.0
     */
    public static @Nullable Material getMaterial(@NotNull final String value) {
        if (value.isEmpty()) {
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.MATERIAL.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid material.");

            return null;
        }
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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.SOUNDS.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid sound.");

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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.ENCHANTMENT.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid enchantment.");

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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.TRIM_PATTERN.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid trim pattern.");

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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.TRIM_MATERIAL.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid trim material.");

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
        return getPotionType(value, true);
    }

    /**
     * Get a {@link PotionType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param warn true or false
     * @return the {@link PotionType} or null if not found
     * @since 1.0
     */
    public static @Nullable PotionType getPotionType(@NotNull final String value, final boolean warn) {
        if (value.isEmpty()) {
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.POTION.get(getKey(value));
        } catch (Exception exception) {
            if (warn) logger.severe(value + " is an invalid potion type.");

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
        return getPotionEffect(value, true);
    }

    /**
     * Get a {@link PotionEffectType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param warn true or false
     * @return the {@link PotionEffectType} or null if not found
     * @since 1.0
     */
    public static @Nullable PotionEffectType getPotionEffect(@NotNull final String value, final boolean warn) {
        if (value.isEmpty()) {
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.POTION_EFFECT_TYPE.get(getKey(value));
        } catch (Exception exception) {
            if (warn) logger.severe(value + " is an invalid potion effect type.");

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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.PARTICLE_TYPE.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid particle type.");

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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.BANNER_PATTERN.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid banner type.");

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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.ENTITY_TYPE.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid entity type.");

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
            logger.severe(value + " cannot be blank!");

            return null;
        }

        try {
            return Registry.ATTRIBUTE.get(getKey(value));
        } catch (Exception exception) {
            logger.severe(value + " is an invalid attribute.");

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
}