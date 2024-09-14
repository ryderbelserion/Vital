package com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents gui types
 *
 * @author Matt
 * @version 0.0.1
 * @since 0.0.1
 */
public enum GuiType {

    /**
     * Chest Inventory
     */
    CHEST(InventoryType.CHEST, 9),

    /**
     * Workbench Inventory
     */
    WORKBENCH(InventoryType.WORKBENCH, 9),

    /**
     * Hopper Inventory
     */
    HOPPER(InventoryType.HOPPER, 5),

    /**
     * Dispenser Inventory
     */
    DISPENSER(InventoryType.DISPENSER, 8),

    /**
     * Brewing Inventory
     */
    BREWING(InventoryType.BREWING, 4);

    private @NotNull final InventoryType inventoryType;
    private final int limit;

    /**
     * Creates an inventory type with a size limit.
     *
     * @param inventoryType {@link InventoryType}
     * @param limit the limit
     */
    GuiType(@NotNull final InventoryType inventoryType, final int limit) {
        this.inventoryType = inventoryType;
        this.limit = limit;
    }

    /**
     * Gets the {@link InventoryType}.
     *
     * @return {@link InventoryType}
     */
    public @NotNull final InventoryType getInventoryType() {
        return this.inventoryType;
    }

    /**
     * The gui size limit
     *
     * @return the gui size limitation
     */
    public final int getLimit() {
        return this.limit;
    }
}