package com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import com.google.common.base.Preconditions;
import com.ryderbelserion.vital.paper.api.builders.gui.types.GuiKeys;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

/**
 * GuiItem represents the {@link ItemStack} on the {@link Inventory}.
 *
 * @author Matt
 * @version 0.0.5
 * @since 0.0.1
 */
@SuppressWarnings("unused")
public class GuiItem {

    // Random UUID to identify the item when clicking
    private final UUID uuid = UUID.randomUUID();
    // Action to do when clicking on the item
    private GuiAction<InventoryClickEvent> action;
    // The ItemStack of the GuiItem
    private ItemStack itemStack;

    /**
     * Main constructor of the {@link GuiItem}.
     *
     * @param itemStack the {@link ItemStack} to be used
     * @param action the {@link GuiAction} to run when clicking on the Item
     */
    public GuiItem(@NotNull final ItemStack itemStack, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        Preconditions.checkNotNull(itemStack, "The ItemStack for the gui Item cannot be null!");

        if (action != null) {
            this.action = action;
        }

        // Sets the UUID to an NBT tag to be identifiable later
        setItemStack(itemStack);
    }

    /**
     * Secondary constructor with no action.
     *
     * @param itemStack the {@link ItemStack} to be used.
     */
    public GuiItem(@NotNull final ItemStack itemStack) {
        this(itemStack, null);
    }

    /**
     * Alternate constructor that takes {@link Material} instead of an {@link ItemStack} but without a {@link GuiAction}.
     *
     * @param material the {@link Material} to be used when invoking class
     */
    public GuiItem(@NotNull final Material material) {
        this(ItemStack.of(material), null);
    }

    /**
     * Alternate constructor that takes {@link Material} instead of an {@link ItemStack}.
     *
     * @param material the {@code Material} to be used when invoking class
     * @param action the {@link GuiAction} should be passed on {@link InventoryClickEvent}
     */
    public GuiItem(@NotNull final Material material, @Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this(ItemStack.of(material), action);
    }

    /**
     * Gets the {@link GuiItem}'s {@link ItemStack}
     *
     * @return the {@link ItemStack}
     */
    public @NotNull final ItemStack getItemStack() {
        return this.itemStack;
    }

    /**
     * Replaces the {@link ItemStack} of the {@link GuiItem}.
     *
     * @param itemStack the new {@link ItemStack}
     */
    public void setItemStack(@NotNull final ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "The ItemStack for the GUI Item cannot be null!");

        if (itemStack.getType() == Material.AIR) {
            this.itemStack = itemStack.clone();

            return;
        }

        final ItemStack item = itemStack.clone();

        item.editMeta(itemMeta -> {
            final PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();

            pdc.set(GuiKeys.key, PersistentDataType.STRING, uuid.toString());
        });

        this.itemStack = item;
    }

    /**
     * Gets the random {@link UUID} that was generated when the {@link GuiItem} was made.
     *
     * @return {@link UUID}
     */
    public @NotNull final UUID getUuid() {
        return this.uuid;
    }

    /**
     * Gets the {@link GuiAction} to do when the player clicks on it.
     *
     * @return {@link GuiAction<InventoryClickEvent>}
     */
    public @Nullable final GuiAction<InventoryClickEvent> getAction() {
        return this.action;
    }

    /**
     * Replaces the {@link GuiAction} of the current {@link GuiItem}.
     *
     * @param action the new {@link GuiAction} to set
     */
    public void setAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        this.action = action;
    }
}
