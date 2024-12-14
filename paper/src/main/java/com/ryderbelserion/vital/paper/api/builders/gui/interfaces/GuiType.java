package com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents gui types
 *
 * @author Matt
 * @version 0.1.0
 * @since 0.1.0
 */
public enum GuiType {

    /**
     * Chest Inventory
     */
    CHEST(InventoryType.CHEST, 9, 9),

    /**
     * Workbench Inventory
     */
    WORKBENCH(InventoryType.WORKBENCH, 9, 10),

    /**
     * Hopper Inventory
     */
    HOPPER(InventoryType.HOPPER, 5, 5),

    /**
     * Dispenser Inventory
     */
    DISPENSER(InventoryType.DISPENSER, 8, 9),

    /**
     * Brewing Inventory
     */
    BREWING(InventoryType.BREWING, 4, 5);

    private @NotNull final InventoryType inventoryType;
    private final int limit;
    private final int fillSize;

    /**
     * Creates an inventory type with a size limit.
     *
     * @param inventoryType {@link InventoryType}
     * @param limit the limit
     */
    GuiType(@NotNull final InventoryType inventoryType, final int limit, final int fillSize) {
        this.inventoryType = inventoryType;
        this.limit = limit;
        this.fillSize = fillSize;
    }

    /**
     * Gets the {@link InventoryType}.
     *
     * @return {@link InventoryType}
     * @since 0.1.0
     */
    public @NotNull final InventoryType getInventoryType() {
        return this.inventoryType;
    }

    /**
     * The fill size
     *
     * @return the fill size
     * @since 0.2.0
     */
    public final int getFillSize() {
        return fillSize;
    }

    /**
     * The gui size limit
     *
     * @return the gui size limitation
     * @since 0.1.0
     */
    public final int getLimit() {
        return this.limit;
    }
}