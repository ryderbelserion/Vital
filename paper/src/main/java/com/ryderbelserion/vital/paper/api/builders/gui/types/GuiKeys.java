package com.ryderbelserion.vital.paper.api.builders.gui.types;

import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the item meta
 *
 * @author ryderbelserion
 * @version 0.0.8
 * @since 0.0.1
 */
public class GuiKeys {

    private static final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(GuiKeys.class);

    /**
     * Handles the item meta
     *
     * @since 0.0.1
     */
    public GuiKeys() {
        throw new AssertionError();
    }

    /**
     * a {@link NamespacedKey} for gui's
     *
     * @since 0.0.1
     */
    public static NamespacedKey key = new NamespacedKey(plugin, "mf-gui");

    /**
     * Get the uuid from the {@link ItemStack}.
     *
     * @param itemStack {@link ItemStack}
     * @return {@link String}
     * @since 0.0.1
     */
    public static @NotNull String getUUID(final ItemStack itemStack) {
        return itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    /**
     * Builds a {@link NamespacedKey}.
     *
     * @param key the key value
     * @return {@link NamespacedKey}
     * @since 0.0.1
     */
    public static @NotNull NamespacedKey build(final String key) {
        return new NamespacedKey(plugin, key);
    }

    /**
     * Strip the {@link NamespacedKey} from the ItemStack.
     *
     * @param itemStack {@link ItemStack}
     * @return {@link ItemStack} without the {@link NamespacedKey}
     * @since 0.0.1
     */
    public static @NotNull ItemStack strip(final ItemStack itemStack) {
        final PersistentDataContainerView container = itemStack.getPersistentDataContainer();

        if (!container.has(key)) {
            return itemStack;
        }

        itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().remove(key));

        return itemStack;
    }
}