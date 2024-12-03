package com.ryderbelserion.vital.paper.api.builders.items.v2;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder extends BaseItemBuilder<ItemBuilder> {

    ItemBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);
    }

    ItemBuilder(@NotNull final String itemStack) {
        super(itemStack);
    }

    public static PotionBuilder potion(@NotNull final ItemType itemType, final int amount) { // if we simply need a potion, only used for hard coding.
        return new PotionBuilder(itemType.createItemStack(amount));
    }

    public static PotionBuilder potion(@NotNull final ItemType itemType) { // if we simply need a potion, only used for hard coding.
        return potion(itemType, 1);
    }

    public static ItemBuilder from(@NotNull final ItemType itemType, final int amount) {
        return new ItemBuilder(itemType.createItemStack(amount));
    }

    public static ItemBuilder from(@NotNull final ItemType itemType) {
        return from(itemType, 1);
    }

    public static ItemBuilder from(@NotNull final ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }

    public static ItemBuilder from(@NotNull final String itemStack) {
        return new ItemBuilder(itemStack);
    }
}