package com.ryderbelserion.vital.paper.api.builders.gui.interfaces.types;

import com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiFiller;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Constraints for the gui classes.
 *
 * @author ryderbelserion
 * @since 0.0.1
 */
public interface IBaseGui {

    /**
     * Gets an immutable {@link Map} with all the GUI items.
     *
     * @return the {@link Map} with all the items
     * @since 0.0.1
     */
    Map<@NotNull Integer, @NotNull GuiItem> getGuiItems();

    /**
     * Gets the title.
     *
     * @return {@link String}
     * @since 0.0.1
     */
    String getTitle();

    /**
     * Sets the new title.
     *
     * @param title the new title
     * @since 0.0.1
     */
    void setTitle(final String title);

    /**
     * Gets the component title.
     *
     * @return {@link Component}
     * @since 0.0.1
     */
    Component title();

    /**
     * Gets the rows.
     *
     * @return the amount of rows
     * @since 0.0.1
     */
    int getRows();

    /**
     * Set how many rows a gui should have.
     *
     * @param rows the number of rows
     * @since 0.0.1
     */
    void setRows(final int rows);

    /**
     * Gets the inventory size.
     *
     * @return the inventory size
     * @since 0.0.1
     */
    int getSize();

    /**
     * Gets the gui type.
     *
     * @return {@link GuiType}
     * @since 0.0.1
     */
    GuiType getGuiType();

    /**
     * Gets the {@link GuiFiller} that it's used for filling up the GUI in specific ways.
     *
     * @return the {@link GuiFiller}
     * @since 0.0.1
     */
    GuiFiller getFiller();

    /**
     * Close an inventory for a player.
     *
     * @param player {@link Player}
     * @param reason {@link InventoryCloseEvent.Reason}
     * @param isDelayed true or false
     * @since 0.0.1
     */
    void close(final Player player, final InventoryCloseEvent.Reason reason, final boolean isDelayed);

    /**
     * Close an inventory for a player.
     *
     * @param player {@link Player}
     * @param isDelayed true or false
     * @since 0.0.1
     */
    void close(final Player player, final boolean isDelayed);

    /**
     * Close an inventory for a player.
     *
     * @param player {@link Player}
     * @since 0.0.1
     */
    void close(final Player player);

    /**
     * Update the inventory title for a single player.
     *
     * @param player {@link Player}
     * @since 0.0.1
     */
    void updateTitle(final Player player);

    /**
     * Update the inventory for a single player.
     *
     * @param player {@link Player}
     * @since 0.0.1
     */
    void updateInventory(final Player player);

    /**
     * Update the inventory titles for all players!
     *
     * @since 0.0.1
     */
    void updateTitles();

    /**
     * Update the inventories for all players!
     *
     * @since 0.0.1
     */
    void updateInventories();

    /**
     * if the menu is updating or not.
     *
     * @return true or false
     * @since 0.0.1
     */
    boolean isUpdating();

    /**
     * Sets the updating status of the gui.
     *
     * @param isUpdating true or false
     * @since 0.0.1
     */
    void setUpdating(final boolean isUpdating);

    /**
     * Adds multiple interaction components.
     *
     * @param components {@link InteractionComponent}
     * @since 0.0.1
     */
    void addInteractionComponent(final InteractionComponent... components);

    /**
     * Removes an interaction component.
     *
     * @param component {@link InteractionComponent}
     * @since 0.0.1
     */
    void removeInteractionComponent(final InteractionComponent component);

    /**
     * Can perform other actions.
     *
     * @return true or false
     * @since 0.0.1
     */
    boolean canPerformOtherActions();

    /**
     * Checks if interaction is disabled.
     *
     * @return true or false
     * @since 0.0.1
     */
    boolean isInteractionsDisabled();

    /**
     * Can place items.
     *
     * @return true or false
     * @since 0.0.1
     */
    boolean canPlaceItems();

    /**
     * Can take items.
     *
     * @return true or false
     * @since 0.0.1
     */
    boolean canTakeItems();

    /**
     * Can swap items.
     *
     * @return true or false
     * @since 0.0.1
     */
    boolean canSwapItems();

    /**
     * Can drop items.
     *
     * @return true or false
     * @since 0.0.1
     */
    boolean canDropItems();

    /**
     * Gives an item to a player while stripping the pdc tag.
     *
     * @param player {@link Player}
     * @param itemStack {@link ItemStack}
     * @since 0.0.1
     */
    void giveItem(final Player player, final ItemStack itemStack);

    /**
     * Gives multiple items to a player while stripping the pdc tag.
     *
     * @param player {@link Player}
     * @param itemStacks {@link ItemStack}
     * @since 0.0.1
     */
    void giveItem(final Player player, final ItemStack... itemStacks);

    /**
     * Sets an item to a specific slot.
     *
     * @param slot the slot number
     * @param guiItem {@link GuiItem}
     * @since 0.0.1
     */
    void setItem(final int slot, final GuiItem guiItem);

    /**
     * Alternative {@link #setItem(int, GuiItem)} to set item that uses <i>ROWS</i> and <i>COLUMNS</i> instead of slots.
     *
     * @param row the GUI row number
     * @param col the GUI column number
     * @param guiItem the {@link GuiItem} to add to the slot
     * @since 0.0.1
     */
    void setItem(final int row, final int col, @NotNull final GuiItem guiItem);

    /**
     * Alternative {@link #setItem(int, GuiItem)} to set item that takes a {@link List} of slots instead.
     *
     * @param slots the slots in which the item should go
     * @param guiItem the {@link GuiItem} to add to the slots
     * @since 0.0.1
     */
    void setItem(@NotNull final List<Integer> slots, @NotNull final GuiItem guiItem);

    /**
     * Adds {@link GuiItem}'s to the GUI without specific slot.
     * It'll set the item to the next empty slot available.
     *
     * @param items varargs for specifying the {@link GuiItem}'s
     * @since 0.0.1
     */
    void addItem(@NotNull final GuiItem... items);

    /**
     * Adds {@link GuiItem}'s to the GUI without specific slot.
     * It'll set the item to the next empty slot available.
     *
     * @param expandIfFull if true, expands the gui if it is full and there are more items to be added
     * @param items varargs for specifying the {@link GuiItem}'s
     * @since 0.0.1
     */
    void addItem(final boolean expandIfFull, @NotNull final GuiItem... items);

    /**
     * Removes an item from the gui.
     *
     * @param itemStack {@link ItemStack}
     * @since 0.0.1
     */
    void removeItem(final ItemStack itemStack);

    /**
     * Alternative {@link #removeItem(int)} with cols and rows.
     *
     * @param row the row
     * @param col the column
     * @since 0.0.1
     */
    void removeItem(final int row, final int col);

    /**
     * Removes an item from the gui.
     *
     * @param guiItem {@link GuiItem}
     * @since 0.0.1
     */
    void removeItem(final GuiItem guiItem);

    /**
     * Removes an item from the inventory based on the slot.
     *
     * @param slot the slot number
     * @since 0.0.1
     */
    void removeItem(final int slot);

    /**
     * Sets the {@link GuiAction} of a default click on any item.
     * See {@link InventoryClickEvent}.
     *
     * @param defaultClickAction {@link GuiAction} to resolve when any item is clicked
     * @since 0.0.1
     */
    void setDefaultClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> defaultClickAction);

    /**
     * Gets a specific {@link GuiItem} on the slot.
     *
     * @param slot the slot of the item.
     * @return the {@link GuiItem} on the introduced slot or {@code null} if it doesn't exist
     * @since 0.0.1
     */
    @Nullable GuiItem getGuiItem(final int slot);

    /**
     * Adds a {@link GuiAction} for when clicking on a specific slot.
     * See {@link InventoryClickEvent}.
     *
     * @param slot the slot that will trigger the {@link GuiAction}.
     * @param slotAction {@link GuiAction} to resolve when clicking on specific slots.
     * @since 0.0.1
     */
    void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction);

    /**
     * Gets the action for the specified slot.
     *
     * @param slot the slot clicked
     * @return {@link GuiAction<InventoryClickEvent>}
     * @since 0.0.1
     */
    @Nullable GuiAction<InventoryClickEvent> getSlotAction(final int slot);

    /**
     * Gets the default top click resolver.
     *
     * @return {@link GuiAction<InventoryClickEvent>}
     * @since 0.0.1
     */
    GuiAction<InventoryClickEvent> getDefaultTopClickAction();

    /**
     * Sets the {@link GuiAction} of a default click on any item on the top part of the GUI.
     * Top inventory being for example chests etc., instead of the {@link Player} inventory.
     * See {@link InventoryClickEvent}.
     *
     * @param defaultTopClickAction {@link GuiAction} to resolve when clicking on the top inventory
     * @since 0.0.1
     */
    void setDefaultTopClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction);

    /**
     * Gets the player inventory action.
     *
     * @return {@link GuiAction<InventoryClickEvent>}
     * @since 0.0.1
     */
    GuiAction<InventoryClickEvent> getPlayerInventoryAction();

    /**
     * Sets the {@link GuiAction} to run when the GUI opens.
     * See {@link InventoryOpenEvent}.
     *
     * @param openGuiAction {@link GuiAction} to resolve when opening the inventory
     * @since 0.0.1
     */
    void setOpenGuiAction(final @Nullable GuiAction<@NotNull InventoryOpenEvent> openGuiAction);

    /**
     * Gets the outside click resolver.
     *
     * @return {@link GuiAction<InventoryClickEvent>}
     * @since 0.0.1
     */
    GuiAction<InventoryClickEvent> getOutsideClickAction();

    /**
     * Gets the default click resolver.
     *
     * @return {@link GuiAction<InventoryClickEvent>}
     * @since 0.0.1
     */
    GuiAction<InventoryClickEvent> getDefaultClickAction();

    /**
     * Sets the {@link GuiAction} of a default drag action.
     * See {@link InventoryDragEvent}.
     *
     * @param dragAction {@link GuiAction} to resolve
     * @since 0.0.1
     */
    void setDragAction(final @Nullable GuiAction<@NotNull InventoryDragEvent> dragAction);

    /**
     * Gets the close gui resolver.
     *
     * @return {@link GuiAction<InventoryCloseEvent>}
     * @since 0.0.1
     */
    GuiAction<InventoryCloseEvent> getCloseGuiAction();

    /**
     * Sets the {@link GuiAction} to run once the inventory is closed.
     * See {@link InventoryCloseEvent}.
     *
     * @param closeGuiAction {@link GuiAction} to resolve when the inventory is closed
     * @since 0.0.1
     */
    void setCloseGuiAction(final @Nullable GuiAction<@NotNull InventoryCloseEvent> closeGuiAction);

    /**
     * Gets the open gui resolver.
     *
     * @return {@link GuiAction<InventoryOpenEvent>}
     * @since 0.0.1
     */
    GuiAction<InventoryOpenEvent> getOpenGuiAction();

    /**
     * Sets the {@link GuiAction} to run when clicking in the inventory.
     * See {@link InventoryClickEvent}.
     *
     * @param playerInventoryAction {@link GuiAction} to resolve when clicking in the inventory
     * @since 0.0.1
     */
    void setPlayerInventoryAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> playerInventoryAction);

    /**
     * Gets the default drag resolver.
     *
     * @return {@link GuiAction<InventoryDragEvent>}
     * @since 0.0.1
     */
    GuiAction<InventoryDragEvent> getDragAction();

    /**
     * Sets the {@link GuiAction} to run when clicking on the outside of the inventory.
     * See {@link InventoryClickEvent}.
     *
     * @param outsideClickAction {@link GuiAction} to resolve when clicking outside the inventory.
     * @since 0.0.1
     */
    void setOutsideClickAction(final @Nullable GuiAction<@NotNull InventoryClickEvent> outsideClickAction);

    /**
     * Opens an inventory for {@link Player}.
     *
     * @param player {@link Player}
     * @param purge true or false
     * @since 0.0.1
     */
    void open(final Player player, final boolean purge);

    /**
     * Opens an inventory for {@link Player}.
     *
     * @param player {@link Player}
     * @since 0.0.1
     */
    void open(final Player player);

    /**
     * Safely copies an enum set.
     *
     * @param components {@link Set<InteractionComponent>}
     * @return {@link Set<InteractionComponent>}
     * @since 0.0.1
     */
    default Set<InteractionComponent> safeCopy(final Set<InteractionComponent> components) {
        return components.isEmpty() ? EnumSet.noneOf(InteractionComponent.class) : EnumSet.copyOf(components);
    }
}