package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.utils.Methods;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Base64;

/**
 * A class containing paper specific methods!
 *
 * @author ryderbelserion
 * @version 2.0.3
 * @since 1.0
 */
public class PaperMethods {

    private static final Vital api = VitalProvider.get();
    private static final ComponentLogger logger = api.getLogger();
    private static final boolean isVerbose = api.isVerbose();

    /**
     * A paper methods class
     *
     * @author ryderbelserion
     * @since 2.0.0
     */
    public PaperMethods() {
        throw new AssertionError();
    }

    /**
     * Updates the inventory title, without re-building the inventory.
     *
     * @param player {@link Player}
     * @param title {@link String}
     * @since 1.0
     */
    public static void updateTitle(@NotNull final Player player, @NotNull final String title) {
        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftHumanEntity) player).getHandle();
        final int containerId = entityPlayer.containerMenu.containerId;
        final MenuType<?> windowType = CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory());
        entityPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(Methods.parse(title)))));

        player.updateInventory();
    }

    /**
     * Fetches the registry access
     *
     * @return {@link RegistryAccess}
     * @since 2.0.3
     */
    public static @NotNull RegistryAccess getRegistryAccess() {
        return RegistryAccess.registryAccess();
    }

    /**
     * Get a {@link ItemType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @return the {@link ItemType} or null if not found
     * @since 2.0.0
     */
    public static @Nullable ItemType getItemType(@NotNull final String value) {
        return getItemType(value, isVerbose);
    }

    /**
     * Get a {@link ItemType} from the {@link Registry}.
     *
     * @param value the {@link String} to check
     * @param isVerbose true or false
     * @return the {@link ItemType} or null if not found
     * @since 2.0.0
     */
    public static @Nullable ItemType getItemType(@NotNull final String value, final boolean isVerbose) {
        if (value.isEmpty()) {
            if (isVerbose) logger.error("{} cannot be blank!", value);

            return null;
        }

        try {
            return getRegistryAccess().getRegistry(RegistryKey.ITEM).get(getKey(value));
        } catch (Exception exception) {
            if (isVerbose) logger.error("{} is an invalid item type.", value);

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
            return getRegistryAccess().getRegistry(RegistryKey.SOUND_EVENT).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.TRIM_PATTERN).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.TRIM_MATERIAL).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.POTION).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.MOB_EFFECT).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.PARTICLE_TYPE).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.ENTITY_TYPE).get(getKey(value));
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
            return getRegistryAccess().getRegistry(RegistryKey.ATTRIBUTE).get(getKey(value));
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