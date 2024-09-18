package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.common.api.Provider;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Base64;

/**
 * All utilities related to items and ids.
 *
 * @author ryderbelserion
 * @version 0.0.6
 * @since 0.0.1
 */
@SuppressWarnings("deprecation")
public class ItemUtil {

    /**
     * Empty constructor
     *
     * @since 0.0.1
     */
    private ItemUtil() {
        throw new AssertionError();
    }

    private static final VitalAPI api = Provider.getApi();
    private static final ComponentLogger logger = api.getComponentLogger();
    private static final boolean isVerbose = api.isVerbose();

    /**
     * Get a {@link Material} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Material} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Material getMaterial(@NotNull final String value) {
        return getMaterial(value, isVerbose);
    }

    /**
     * Get a {@link Material} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link Material} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Material getMaterial(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.MATERIAL.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid material.", value);

            return null;
        }
    }

    /**
     * Get a {@link Sound} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Sound} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Sound getSound(@NotNull final String value) {
        return getSound(value, isVerbose);
    }

    /**
     * Get a {@link Sound} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link Sound} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Sound getSound(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.SOUNDS.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid sound.", value);

            return null;
        }
    }

    /**
     * Get an {@link Enchantment} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Enchantment} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Enchantment getEnchantment(@NotNull final String value) {
        return getEnchantment(value, isVerbose);
    }

    /**
     * Get an {@link Enchantment} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link Enchantment} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Enchantment getEnchantment(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid enchantment.", value);

            return null;
        }
    }

    /**
     * Get a {@link TrimPattern} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link TrimPattern} or null if not found
     * @since 0.0.1
     */
    public static @Nullable TrimPattern getTrimPattern(@NotNull final String value) {
        return getTrimPattern(value, isVerbose);
    }

    /**
     * Get a {@link TrimPattern} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link TrimPattern} or null if not found
     * @since 0.0.1
     */
    public static @Nullable TrimPattern getTrimPattern(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid trim pattern.", value);

            return null;
        }
    }

    /**
     * Get a {@link TrimMaterial} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link TrimMaterial} or null if not found
     * @since 0.0.1
     */
    public static @Nullable TrimMaterial getTrimMaterial(@NotNull final String value) {
        return getTrimMaterial(value, isVerbose);
    }

    /**
     * Get a {@link TrimMaterial} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link TrimMaterial} or null if not found
     * @since 0.0.1
     */
    public static @Nullable TrimMaterial getTrimMaterial(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid trim material.", value);

            return null;
        }
    }

    /**
     * Get a {@link PotionType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link PotionType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable PotionType getPotionType(@NotNull final String value) {
        return getPotionType(value, isVerbose);
    }

    /**
     * Get a {@link PotionType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link PotionType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable PotionType getPotionType(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.POTION.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid potion type.", value);

            return null;
        }
    }

    /**
     * Get a {@link PotionEffectType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link PotionEffectType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable PotionEffectType getPotionEffect(@NotNull final String value) {
        return getPotionEffect(value, isVerbose);
    }

    /**
     * Get a {@link PotionEffectType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link PotionEffectType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable PotionEffectType getPotionEffect(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.POTION_EFFECT_TYPE.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid potion effect type.", value);

            return null;
        }
    }

    /**
     * Get a {@link Particle} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Particle} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Particle getParticleType(@NotNull final String value) {
        return getParticleType(value, isVerbose);
    }

    /**
     * Get a {@link Particle} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link Particle} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Particle getParticleType(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.PARTICLE_TYPE.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid particle type.", value);

            return null;
        }
    }

    /**
     * Get a {@link PatternType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link PatternType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable PatternType getPatternType(@NotNull final String value) {
        return getPatternType(value, isVerbose);
    }

    /**
     * Get a {@link PatternType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link PatternType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable PatternType getPatternType(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.BANNER_PATTERN.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid banner type.", value);

            return null;
        }
    }

    /**
     * Gets an {@link EntityType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link EntityType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable EntityType getEntity(@NotNull final String value) {
        return getEntity(value, isVerbose);
    }

    /**
     * Gets an {@link EntityType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link EntityType} or null if not found
     * @since 0.0.1
     */
    public static @Nullable EntityType getEntity(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.ENTITY_TYPE.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid entity type.", value);

            return null;
        }
    }

    /**
     * Get an {@link Attribute} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link Attribute} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Attribute getAttribute(@NotNull final String value) {
        return getAttribute(value, isVerbose);
    }

    /**
     * Get an {@link Attribute} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link Attribute} or null if not found
     * @since 0.0.1
     */
    public static @Nullable Attribute getAttribute(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return Registry.ATTRIBUTE.get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid attribute.", value);

            return null;
        }
    }

    /**
     * Check the {@link NamespacedKey}.
     *
     * @param value the {@link String} to check
     * @return {@link NamespacedKey}
     * @since 0.0.1
     */
    private static @NotNull NamespacedKey getKey(@NotNull final String value) {
        return NamespacedKey.minecraft(value);
    }

    /**
     * Encodes and serializes an {@link ItemStack} into bytes
     *
     * @param itemStack {@link ItemStack}
     * @return bytes
     * @since 0.0.2
     */
    public static byte[] toBytes(@NotNull final ItemStack itemStack) {
        return itemStack.serializeAsBytes();
    }

    /**
     * Deserialize bytes back into an {@link ItemStack}.
     *
     * @param bytes the bytes
     * @return {@link ItemStack}
     * @since 0.0.2
     */
    public static @NotNull ItemStack fromBytes(final byte @NotNull [] bytes) {
        return ItemStack.deserializeBytes(bytes);
    }

    /**
     * Encodes and serializes an {@link ItemStack} into a {@link String}.
     *
     * @param itemStack {@link ItemStack}
     * @return {@link String}
     * @since 0.0.1
     */
    public static String toBase64(@NotNull final ItemStack itemStack) {
        return Base64.getEncoder().encodeToString(itemStack.serializeAsBytes());
    }

    /**
     * Deserialize a base64 {@link String} back into an {@link ItemStack}.
     *
     * @param base64 the base64 {@link String}
     * @return {@link ItemStack}
     * @since 0.0.1
     */
    public static @NotNull ItemStack fromBase64(@NotNull final String base64) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(base64));
    }
}