package com.ryderbelserion.vital.paper.api.builders.items.v2;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseItemBuilder<B extends BaseItemBuilder<B>> {

    protected final Vital api = VitalProvider.get();

    private ItemStack itemStack;

    protected BaseItemBuilder(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Turns the builder into {@link ItemStack}.
     *
     * @return the fully built {@link ItemStack}
     * @since 0.2.0
     */
    public ItemStack asItemStack() {
        if (!this.displayName.isBlank()) {
            this.itemStack.setData(DataComponentTypes.ITEM_NAME, this.api.color(this.displayName));
        }

        if (!this.displayLore.isEmpty()) {
            final List<Component> components = new ArrayList<>();

            this.displayLore.forEach(line -> components.add(this.api.color(line)));

            final ItemLore lore = ItemLore.lore(components);

            this.itemStack.setData(DataComponentTypes.LORE, lore);
        }

        return this.itemStack;
    }

    /**
     * Sets the {@link ItemType} type.
     *
     * @param itemType the {@link ItemType} to set
     * @param amount the amount of {@link ItemType}
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withType(@Nullable final ItemType itemType, final int amount) {
        if (itemType == null) return (B) this;

        this.itemStack = itemType.createItemStack(amount);

        return (B) this;
    }

    /**
     * Sets the {@link ItemType} type.
     *
     * @param itemType the {@link ItemType} to set
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withType(@Nullable final ItemType itemType) {
        return withType(itemType, 1);
    }

    /**
     * Sets the amount an item should be.
     *
     * @param amount the amount
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withAmount(int amount) {
        if (amount <= 0) amount = 1;

        this.itemStack.setAmount(amount);

        return (B) this;
    }

    public B addEnchantment(@NotNull final String enchant, final int level, final boolean ignoreLevelCap, final boolean showToolTip) {
        if (enchant.isEmpty()) return (B) this;

        final ItemEnchantments enchantments = ItemEnchantments.itemEnchantments().build();

        this.itemStack.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments().build());

        return (B) this;
    }

    private String displayName = "";

    /**
     * Sets the display name.
     *
     * @param displayName the {@link String} to use
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withDisplayName(@NotNull final String displayName) {
        this.displayName = displayName;

        return (B) this;
    }

    private List<String> displayLore = new ArrayList<>();

    /**
     * Add to the display lore.
     *
     * @param displayLore the {@link String}
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B addDisplayLore(@NotNull final String displayLore) {
        if (displayLore.isEmpty()) return (B) this;

        this.displayLore.add(displayLore);

        return (B) this;
    }

    /**
     * Override the display lore.
     *
     * @param displayLore the {@link List<String>}
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withDisplayLore(@NotNull final List<String> displayLore) {
        this.displayLore = displayLore;

        return (B) this;
    }

    /**
     * Adds enchant glint to the item.
     *
     * @param enchantGlintOverride true or false or null
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B setEnchantGlint(final boolean enchantGlintOverride) {
        this.itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchantGlintOverride);

        return (B) this;
    }

    /**
     * Removes enchant glint from the item.
     *
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B removeEnchantGlint() {
        this.itemStack.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);

        return (B) this;
    }

    /**
     * Adds the item to the inventory at a specific slot.
     *
     * @param inventory {@link Inventory}
     * @param slot the slot
     * @since 0.2.0
     */
    public void setItemToInventory(final Inventory inventory, final int slot) {
        inventory.setItem(slot, this.itemStack);
    }

    /**
     * Adds the item to the inventory.
     *
     * @param inventory {@link Inventory}
     * @since 0.2.0
     */
    public void addItemToInventory(final Inventory inventory) {
        inventory.addItem(this.itemStack);
    }

    /**
     * Creates an instance of {@link PotionBuilder}.
     *
     * @return {@link PotionBuilder}
     * @since 0.2.0
     */
    public PotionBuilder asPotionBuilder() {
        return new PotionBuilder(this.itemStack);
    }

    /**
     * Converts {@link ItemStack} to {@link GuiItem}.
     *
     * @param action runs a click event
     * @return {@link GuiItem}
     * @since 0.2.0
     */
    public GuiItem asGuiItem(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        return new GuiItem(asItemStack(), action);
    }

    /**
     * Converts {@link ItemStack} to {@link GuiItem}.
     *
     * @return {@link GuiItem}
     * @since 0.2.0
     */
    public GuiItem asGuiItem() {
        return new GuiItem(asItemStack(), null);
    }

    /**
     * Gets the {@link ItemType} type.
     *
     * @return the {@link ItemType} type
     * @since 0.1.0
     */
    protected final @NotNull Material getType() {
        return this.itemStack.getType();
    }

    /**
     * Gets the current ItemStack instance.
     *
     * @return {@link ItemStack}
     * @since 0.2.0
     */
    protected @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }
}