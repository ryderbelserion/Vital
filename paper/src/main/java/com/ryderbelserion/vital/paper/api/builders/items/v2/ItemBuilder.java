package com.ryderbelserion.vital.paper.api.builders.items.v2;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} is an experimental class that extends {@link BaseItemBuilder} for creating item builders.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
@ApiStatus.Experimental
public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    /**
     * Constructs an {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder}  with the provided ItemStack.
     *
     * @param itemStack the {@link ItemStack} to be wrapped by this builder, must not be null
     * @since 0.1.0
     */
    ItemBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    /**
     * Constructs an {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} with the provided value.
     *
     * @param value the value representing the item, must not be null
     * @since 0.1.0
     */
    ItemBuilder(@NotNull final String value) {
        super(value);
    }

    /**
     * Creates a new {@link PotionBuilder} with the specified {@link ItemType} and amount.
     *
     * @param itemType the type of the item, must not be null
     * @param amount the amount of the item
     * @return a new {@link PotionBuilder} instance
     * @since 0.1.0
     */
    public static PotionBuilder potion(@NotNull final ItemType itemType, final int amount) {
        return new PotionBuilder(itemType.createItemStack(amount));
    }

    /**
     * Creates a new {@link PotionBuilder} with the specified {@link ItemType} and a default amount of 1.
     *
     * @param itemType the type of the item, must not be null
     * @return a new {@link PotionBuilder} instance
     * @since 0.1.0
     */
    public static PotionBuilder potion(@NotNull final ItemType itemType) {
        return potion(itemType, 1);
    }

    /**
     * Creates a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} with the specified {@link ItemType} and amount.
     *
     * @param itemType the type of the item, must not be null
     * @param amount the amount of the item
     * @return a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} instance
     * @since 0.1.0
     */
    public static ItemBuilder from(@NotNull final ItemType itemType, final int amount) {
        return new ItemBuilder(itemType.createItemStack(amount));
    }

    /**
     * Creates a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} with the provided ItemStack.
     *
     * @param itemStack the ItemStack to be wrapped by this builder, must not be null
     * @return a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} instance
     * @since 0.1.0
     */
    public static ItemBuilder from(@NotNull final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    /**
     * Creates a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} with the specified ItemType and a default amount of 1.
     *
     * @param itemType the type of the item, must not be null
     * @return a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} instance
     * @since 0.1.0
     */
    public static ItemBuilder from(@NotNull final ItemType itemType) {
        return from(itemType, 1);
    }

    /**
     * Creates a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} with the provided value.
     *
     * @param value the value representing the item, must not be null
     * @return a new {@link com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder} instance
     * @since 0.1.0
     */
    public static ItemBuilder from(@NotNull final String value) {
        return new ItemBuilder(value);
    }
}