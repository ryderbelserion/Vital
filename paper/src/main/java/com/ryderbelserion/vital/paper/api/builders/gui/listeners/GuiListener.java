package com.ryderbelserion.vital.paper.api.builders.gui.listeners;

import com.google.common.base.Preconditions;
import com.ryderbelserion.vital.paper.api.builders.gui.types.BaseGui;
import com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import com.ryderbelserion.vital.paper.api.builders.gui.types.GuiKeys;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Handles all gui interactions.
 *
 * @author Matt
 * @version 0.0.1
 * @since 0.0.1
 */
public class GuiListener implements Listener {

    /**
     * Handles all gui interactions.
     *
     * @since 0.0.1
     */
    public GuiListener() {}

    /**
     * Handles inventory clicks.
     *
     * @param event {@link InventoryClickEvent}
     * @since 0.0.1
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof BaseGui gui)) return;

        // if all interactions are disabled.
        if (gui.isInteractionsDisabled()) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        } else {
            // if player is trying to do a disabled action, cancel it
            if ((!gui.canPlaceItems() && isPlaceItemEvent(event)) ||
                    (!gui.canTakeItems() && isTakeItemEvent(event)) || (!gui.canSwapItems() && isSwapItemEvent(event)) ||
                    (!gui.canDropItems() && isDropItemEvent(event)) || (!gui.canPerformOtherActions() && isOtherEvent(event))) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }

        if (event.getClickedInventory() == null) return;

        // Default click action and checks weather or not there is a default action and executes it
        final GuiAction<InventoryClickEvent> defaultTopClick = gui.getDefaultTopClickAction();

        if (defaultTopClick != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            defaultTopClick.execute(event);
        }

        // Default click action and checks weather or not there is a default action and executes it
        final GuiAction<InventoryClickEvent> playerInventoryClick = gui.getPlayerInventoryAction();

        if (playerInventoryClick != null && event.getClickedInventory().getType() == InventoryType.PLAYER) {
            playerInventoryClick.execute(event);
        }

        // Default click action and checks weather or not there is a default action and executes it
        final GuiAction<InventoryClickEvent> defaultClick = gui.getDefaultClickAction();

        if (defaultClick != null) defaultClick.execute(event);

        // Slot action and checks weather or not there is a slot action and executes it
        final GuiAction<InventoryClickEvent> slotAction = gui.getSlotAction(event.getSlot());

        if (slotAction != null && event.getClickedInventory().getType() != InventoryType.PLAYER) {
            slotAction.execute(event);
        }

        GuiItem guiItem;

        // Checks whether it's a paginated gui or not
        if (gui instanceof PaginatedGui paginatedGui) {
            // Gets the gui item from the added items or the page items
            guiItem = paginatedGui.getGuiItem(event.getSlot());

            if (guiItem == null) guiItem = paginatedGui.getPageItem(event.getSlot());
        } else {
            // The clicked GUI Item
            guiItem = gui.getGuiItem(event.getSlot());
        }

        if (!isGuiItem(event.getCurrentItem(), guiItem)) return;

        // Executes the action of the item
        final GuiAction<InventoryClickEvent> itemAction = guiItem.getAction();
        if (itemAction != null) itemAction.execute(event);
    }

    /**
     * Handles inventory drag.
     *
     * @param event {@link InventoryDragEvent}
     * @since 0.0.1
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof BaseGui gui)) return;

        if (gui.isInteractionsDisabled()) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);

            return;
        }

        // if players are allowed to place items on the GUI, or player is not dragging on GUI, return
        if (gui.canPlaceItems() || !isDraggingOnGui(event)) return;

        // cancel the interaction
        event.setCancelled(true);
        event.setResult(Event.Result.DENY);
    }

    /**
     * Closes the inventory.
     *
     * @param event {@link InventoryCloseEvent}
     * @since 0.0.1
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof BaseGui gui)) return;

        final GuiAction<InventoryCloseEvent> closeAction = gui.getCloseGuiAction();

        if (closeAction != null && !gui.isUpdating()) closeAction.execute(event);
    }

    /**
     * Handles what happens when the GUI is opened.
     *
     * @param event {@link InventoryOpenEvent}
     * @since 0.0.1
     */
    @EventHandler
    public void onGuiOpen(final InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof BaseGui gui)) return;

        final GuiAction<InventoryOpenEvent> openAction = gui.getOpenGuiAction();

        if (openAction != null && !gui.isUpdating()) openAction.execute(event);
    }

    /**
     * Checks if the item is or not a GUI item.
     *
     * @param currentItem the current item clicked
     * @param guiItem the {@link GuiItem} in the slot
     * @return true or false
     * @since 0.0.1
     */
    private boolean isGuiItem(@Nullable final ItemStack currentItem, @Nullable final GuiItem guiItem) {
        if (currentItem == null || guiItem == null) {
            return false;
        }

        final String nbt = GuiKeys.getUUID(currentItem);

        if (nbt.isEmpty() || nbt.isBlank()) {
            return false;
        }

        return nbt.equalsIgnoreCase(guiItem.getUuid().toString());
    }

    /**
     * Checks if what is happening on the {@link InventoryClickEvent} is taking an item from the gui.
     *
     * @param event {@link InventoryClickEvent}
     * @return true if the {@link InventoryClickEvent} is taking an item from the gui
     * @author SecretX
     * @since 0.0.1
     */
    private boolean isTakeItemEvent(final InventoryClickEvent event) {
        Preconditions.checkNotNull(event, "event cannot be null");

        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        // magic logic, simplified version of https://paste.helpch.at/tizivomeco.cpp
        if (clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER || inventory.getType() == InventoryType.PLAYER) {
            return false;
        }

        return action == InventoryAction.MOVE_TO_OTHER_INVENTORY || isTakeAction(action);
    }

    /**
     * Checks if what is happening on the {@link InventoryClickEvent} is placing an item in the gui.
     *
     * @param event {@link InventoryClickEvent}
     * @return true if the {@link InventoryClickEvent} is placing an item into the gui
     * @author SecretX
     * @since 0.0.1
     */
    private boolean isPlaceItemEvent(final InventoryClickEvent event) {
        Preconditions.checkNotNull(event, "event cannot be null");

        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        // shift click on item in player inventory
        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY && clickedInventory != null && clickedInventory.getType() == InventoryType.PLAYER && inventory.getType() != clickedInventory.getType()) {
            return true;
        }

        // normal click on gui empty slot with item on cursor
        return isPlaceAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    /**
     * Checks if what is happening on the {@link InventoryClickEvent} is swap any item with an item from the gui.
     *
     * @param event {@link InventoryClickEvent}
     * @return true if the {@link InventoryClickEvent} is for swapping any item with an item from the gui
     * @author SecretX
     * @since 0.0.1
     */
    private boolean isSwapItemEvent(final InventoryClickEvent event) {
        Preconditions.checkNotNull(event, "event cannot be null");

        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        return isSwapAction(action) && (clickedInventory == null || clickedInventory.getType() != InventoryType.PLAYER) && inventory.getType() != InventoryType.PLAYER;
    }

    /**
     * Checks if what is happening on the {@link InventoryClickEvent} is dropping an item from the gui.
     *
     * @param event {@link InventoryClickEvent}
     * @return true if the {@link InventoryClickEvent} is dropping an item from the gui
     * @author SecretX
     * @since 0.0.1
     */
    private boolean isDropItemEvent(final InventoryClickEvent event) {
        Preconditions.checkNotNull(event, "event cannot be null");

        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        return isDropAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
    }

    /**
     * Checks if what is happening on the {@link InventoryClickEvent} is doing something else.
     *
     * @param event {@link InventoryClickEvent}
     * @return true if the {@link InventoryClickEvent} is doing something else
     * @author SecretX
     * @since 0.0.1
     */
    private boolean isOtherEvent(final InventoryClickEvent event) {
        Preconditions.checkNotNull(event, "event cannot be null");

        final Inventory inventory = event.getInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        final InventoryAction action = event.getAction();

        return isOtherAction(action) && (clickedInventory != null || inventory.getType() != InventoryType.PLAYER);
    }

    /**
     * Checks if any item is being dragged on the gui.
     *
     * @param event {@link InventoryDragEvent}
     * @return true if the {@link InventoryDragEvent} is for dragging an item inside the gui
     * @author SecretX
     * @since 0.0.1
     */
    private boolean isDraggingOnGui(final InventoryDragEvent event) {
        Preconditions.checkNotNull(event, "event cannot be null");

        final int topSlots = event.getView().getTopInventory().getSize();

        // is dragging on any top inventory slot
        return event.getRawSlots().stream().anyMatch(slot -> slot < topSlots);
    }

    /**
     * Checks if it is a take action.
     *
     * @param action {@link InventoryAction}
     * @return true or false
     * @since 0.0.1
     */
    private boolean isTakeAction(final InventoryAction action) {
        Preconditions.checkNotNull(action, "action cannot be null");

        return ITEM_TAKE_ACTIONS.contains(action);
    }

    /**
     * Checks if it is a place action.
     *
     * @param action {@link InventoryAction}
     * @return true or false
     * @since 0.0.1
     */
    private boolean isPlaceAction(final InventoryAction action) {
        Preconditions.checkNotNull(action, "action cannot be null");

        return ITEM_PLACE_ACTIONS.contains(action);
    }

    /**
     * Checks if it is a swap action.
     *
     * @param action {@link InventoryAction}
     * @return true or false
     * @since 0.0.1
     */
    private boolean isSwapAction(final InventoryAction action) {
        Preconditions.checkNotNull(action, "action cannot be null");

        return ITEM_SWAP_ACTIONS.contains(action);
    }

    /**
     * Checks if it is a drop action.
     *
     * @param action {@link InventoryAction}
     * @return true or false
     * @since 0.0.1
     */
    private boolean isDropAction(final InventoryAction action) {
        Preconditions.checkNotNull(action, "action cannot be null");

        return ITEM_DROP_ACTIONS.contains(action);
    }

    /**
     * Checks if it is another action.
     *
     * @param action {@link InventoryAction}
     * @return true or false
     * @since 0.0.1
     */
    private boolean isOtherAction(final InventoryAction action) {
        Preconditions.checkNotNull(action, "action cannot be null");

        return action == InventoryAction.CLONE_STACK || action == InventoryAction.UNKNOWN;
    }

    /**
     * Holds all the actions that should be considered "take" actions.
     *
     * @since 0.0.1
     */
    private static final Set<InventoryAction> ITEM_TAKE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ALL, InventoryAction.COLLECT_TO_CURSOR, InventoryAction.HOTBAR_SWAP, InventoryAction.MOVE_TO_OTHER_INVENTORY));

    /**
     * Holds all actions relating to dropping items.
     *
     * @since 0.0.1
     */
    private static final Set<InventoryAction> ITEM_DROP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_CURSOR, InventoryAction.DROP_ALL_CURSOR));

    /**
     * Holds all the actions that should be considered "place" actions.
     *
     * @since 0.0.1
     */
    private static final Set<InventoryAction> ITEM_PLACE_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.PLACE_ALL));

    /**
     * Holds all actions relating to swapping items.
     *
     * @since 0.0.1
     */
    private static final Set<InventoryAction> ITEM_SWAP_ACTIONS = Collections.unmodifiableSet(EnumSet.of(InventoryAction.HOTBAR_SWAP, InventoryAction.SWAP_WITH_CURSOR));
}