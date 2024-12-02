package com.ryderbelserion.vital.paper.api.builders.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Handles persistent data container (PDC) operations on item stacks.
 * This class provides methods for setting, getting, and removing data stored in item stacks using namespaced keys.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class NbtBuilder {

    private ItemStack itemStack;

    /**
     * Constructs a new {@link ItemStack} with a dummy {@link ItemType}.
     *
     * @since 0.1.0
     */
    public NbtBuilder() {
        this(ItemType.STONE, 1);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link ItemType}.
     *
     * @param itemType the {@link ItemType} to use
     * @since 0.1.0
     */
    public NbtBuilder(@NotNull final ItemType itemType) {
        this(itemType, 1);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link ItemType} and amount.
     *
     * @param itemType the {@link ItemType} to use
     * @param amount the amount to set
     * @since 0.1.0
     */
    public NbtBuilder(@NotNull final ItemType itemType, final int amount) {
        this(itemType.createItemStack(amount), true);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified item stack and an option to create a new stack.
     *
     * @param itemStack the {@link ItemStack}
     * @param createNewStack true to create a new item stack, false to reuse the passed object
     * @since 0.1.0
     */
    public NbtBuilder(@NotNull final ItemStack itemStack, final boolean createNewStack) {
        this.itemStack = createNewStack ? itemStack.clone() : itemStack;
    }

    /**
     * Checks if the {@link ItemStack} has {@link ItemMeta}.
     *
     * @return true if the item stack has meta data, false otherwise
     * @since 0.1.0
     */
    public final boolean hasItemMeta() {
        return !this.itemStack.hasItemMeta();
    }

    /**
     * Sets the {@link ItemStack}.
     *
     * @param itemStack the item stack to set
     * @return the current {@link NbtBuilder} instance for chaining
     * @since 0.1.0
     */
    public @NotNull final NbtBuilder setItemStack(final ItemStack itemStack) {
        this.itemStack = itemStack;

        return this;
    }

    /**
     * Adds a {@link Double} to the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Double} to set
     * @return the current {@link NbtBuilder} instance for chaining
     * @since 0.1.0
     */
    public @NotNull final NbtBuilder setPersistentDouble(@NotNull final NamespacedKey key, final double value) {
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value));

        return this;
    }

    /**
     * Adds a {@link Integer} to the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Integer} to set
     * @return the current {@link NbtBuilder} instance for chaining
     * @since 0.1.0
     */
    public @NotNull final NbtBuilder setPersistentInteger(@NotNull final NamespacedKey key, final int value) {
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, value));

        return this;
    }

    /**
     * Adds a {@link Boolean} to the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Boolean} to set
     * @return the current {@link NbtBuilder} instance for chaining
     * @since 0.1.0
     */
    public @NotNull final NbtBuilder setPersistentBoolean(@NotNull final NamespacedKey key, final boolean value) {
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(key, PersistentDataType.BOOLEAN, value));

        return this;
    }

    /**
     * Adds a {@link String} to the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link String} to set
     * @return the current {@link NbtBuilder} instance for chaining
     * @since 0.1.0
     */
    public @NotNull final NbtBuilder setPersistentString(@NotNull final NamespacedKey key, @NotNull final String value) {
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, value));

        return this;
    }

    /**
     * Adds a {@link List<String>} to the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey} for the data
     * @param values the {@link List<String>} to store
     * @return the {@link NbtBuilder} instance for chaining
     * @since 0.1.0
     */
    public @NotNull final NbtBuilder setPersistentList(@NotNull final NamespacedKey key, @NotNull final List<String> values) {
        // This must always apply the item meta as we are adding to the storage so item meta will be created anyway.
        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(
                key,
                PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING),
                values
        ));

        return this;
    }

    /**
     * Gets a {@link Boolean} from the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey} for the data
     * @return the boolean value, or false if not present
     * @since 0.1.0
     */
    public final boolean getBoolean(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false);
    }

    /**
     * Gets a {@link Double} from the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey} for the data
     * @return the double value, or 0.0 if not present
     * @since 0.1.0
     */
    public final double getDouble(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.DOUBLE, 0.0);
    }

    /**
     * Gets an {@link Integer} from the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey} for the data
     * @return the integer value, or 0 if not present
     * @since 0.1.0
     */
    public final int getInteger(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 0);
    }

    /**
     * Gets a {@link List<String>} from the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey} for the data
     * @return the list of strings, or an empty list if not present
     * @since 0.1.0
     */
    public @NotNull final List<String> getList(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.LIST.strings(), Collections.emptyList());
    }

    /**
     * Gets a {@link String} from the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey} for the data
     * @return the string value, or an empty string if not present
     * @since 0.1.0
     */
    public @NotNull final String getString(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().getOrDefault(key, PersistentDataType.STRING, "");
    }

    /**
     * Removes a {@link NamespacedKey} from the {@link ItemStack}'s {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey} to remove
     * @return the {@link NbtBuilder} instance for chaining
     * @since 0.1.0
     */
    public @NotNull final NbtBuilder removePersistentKey(@Nullable final NamespacedKey key) {
        if (key == null) return this;

        if (!this.itemStack.getPersistentDataContainer().has(key)) return this;

        this.itemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().remove(key));

        return this;
    }

    /**
     * Checks if the {@link ItemStack} has a specific {@link NamespacedKey}.
     *
     * @param key the {@link NamespacedKey} to check for
     * @return true if the {@link NamespacedKey} is present, false otherwise
     * @since 0.1.0
     */
    public final boolean hasKey(@NotNull final NamespacedKey key) {
        return this.itemStack.getPersistentDataContainer().has(key);
    }

    /**
     * Returns the {@link ItemStack}.
     *
     * @return the {@link ItemStack}
     * @since 0.1.0
     */
    public final ItemStack getItemStack() {
        return this.itemStack;
    }
}