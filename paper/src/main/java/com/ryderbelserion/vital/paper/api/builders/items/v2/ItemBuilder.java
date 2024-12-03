package com.ryderbelserion.vital.paper.api.builders.items.v2;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Experimental
public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    ItemBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    ItemBuilder(@NotNull final String value) {
        super(value);
    }

    public static PotionBuilder potion(@NotNull final ItemType itemType, final int amount) {
        return new PotionBuilder(itemType.createItemStack(amount));
    }

    public static PotionBuilder potion(@NotNull final ItemType itemType) {
        return potion(itemType, 1);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType, final int amount) {
        return new ItemBuilder(itemType.createItemStack(amount));
    }

    public static ItemBuilder from(@NotNull final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType) {
        return from(itemType, 1);
    }

    public static ItemBuilder from(@NotNull final String value) {
        return new ItemBuilder(value);
    }
}