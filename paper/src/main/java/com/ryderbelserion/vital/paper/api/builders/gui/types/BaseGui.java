package com.ryderbelserion.vital.paper.api.builders.gui.types;

import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiFiller;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.types.IBaseGui;
import com.ryderbelserion.vital.paper.util.PaperMethods;
import com.ryderbelserion.vital.paper.util.scheduler.impl.FoliaScheduler;
import com.ryderbelserion.vital.schedulers.enums.SchedulerType;
import com.ryderbelserion.vital.utils.Methods;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Creates a simple gui.
 *
 * @author Matt
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class BaseGui implements InventoryHolder, Listener, IBaseGui {

    private final JavaPlugin plugin = VitalPaper.getPlugin();

    // Gui filler.
    private final GuiFiller filler = new GuiFiller(this);
    // Actions for specific slots.
    private final Map<Integer, GuiAction<InventoryClickEvent>> slotActions;
    // Contains all items the GUI will have.
    private final Map<Integer, GuiItem> guiItems;

    private final Set<InteractionComponent> interactionComponents;

    // Action to execute when clicking on the top part of the GUI only.
    private GuiAction<InventoryClickEvent> defaultTopClickAction;
    // Action to execute when clicking on the player Inventory.
    private GuiAction<InventoryClickEvent> playerInventoryAction;
    // Action to execute when clicked outside the GUI.
    private GuiAction<InventoryClickEvent> outsideClickAction;
    // Action to execute when clicking on any item.
    private GuiAction<InventoryClickEvent> defaultClickAction;
    // Action to execute when GUI closes.
    private GuiAction<InventoryCloseEvent> closeGuiAction;
    // Action to execute when GUI opens.
    private GuiAction<InventoryOpenEvent> openGuiAction;
    // Action to execute when dragging the item on the GUI.
    private GuiAction<InventoryDragEvent> dragAction;

    private GuiType guiType = GuiType.CHEST;

    private final Inventory inventory;
    private boolean isUpdating;
    private String title;
    private int rows;

    /**
     * The main constructor, using {@link String}.
     *
     * @param rows the amount of rows
     * @param title {@link String}
     * @param components modifiers to select which interactions are allowed
     * @since 0.1.0
     */
    public BaseGui(@NotNull final String title, final int rows, final Set<InteractionComponent> components) {
        this.title = title;
        this.rows = rows;

        int size = this.rows * 9;

        this.slotActions = new LinkedHashMap<>(size);
        this.guiItems = new LinkedHashMap<>(size);

        this.interactionComponents = safeCopy(components);

        this.inventory = plugin.getServer().createInventory(this, size, title());
    }

    /**
     * Alternative constructor that takes {@link GuiType} instead of rows number.
     *
     * @param guiType the {@link GuiType}
     * @param title {@link String}
     * @param components modifiers to select which interactions are allowed
     * @since 0.1.0
     */
    public BaseGui(@NotNull final String title, final GuiType guiType, final Set<InteractionComponent> components) {
        this.title = title;

        this.slotActions = new LinkedHashMap<>(guiType.getLimit());
        this.guiItems = new LinkedHashMap<>(guiType.getLimit());

        this.interactionComponents = safeCopy(components);

        this.inventory = plugin.getServer().createInventory(this, guiType.getInventoryType(), title());

        this.guiType = guiType;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final Map<Integer, GuiItem> getGuiItems() {
        return Collections.unmodifiableMap(this.guiItems);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull final String getTitle() {
        return PlainTextComponentSerializer.plainText().serialize(title());
    }

    /**
     * {@inheritDoc}
     *
     * @param title {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setTitle(@NotNull final String title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull final Component title() {
        return Methods.parse(this.title);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final int getRows() {
        return this.rows;
    }

    /**
     * {@inheritDoc}
     *
     * @param rows {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setRows(final int rows) {
        this.rows = rows;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final int getSize() {
        return getRows() * 9;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final GuiType getGuiType() {
        return this.guiType;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final GuiFiller getFiller() {
        return this.filler;
    }

    /**
     * {@inheritDoc}
     *
     * @param components {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void addInteractionComponent(final InteractionComponent... components) {
        this.interactionComponents.addAll(Arrays.asList(components));
    }

    /**
     * {@inheritDoc}
     *
     * @param component {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void removeInteractionComponent(final InteractionComponent component) {
        this.interactionComponents.remove(component);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean canPerformOtherActions() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_OTHER_ACTIONS);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean isInteractionsDisabled() {
        return this.interactionComponents.size() == InteractionComponent.VALUES.size();
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean canPlaceItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_PLACE);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean canTakeItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_TAKE);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean canSwapItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_SWAP);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean canDropItems() {
        return !this.interactionComponents.contains(InteractionComponent.PREVENT_ITEM_DROP);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param itemStack {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void giveItem(final Player player, final ItemStack itemStack) {
        player.getInventory().addItem(GuiKeys.strip(itemStack));
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param itemStacks {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void giveItem(final Player player, final ItemStack... itemStacks) {
        Arrays.asList(itemStacks).forEach(item -> giveItem(player, item));
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param guiItem {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setItem(final int slot, final GuiItem guiItem) {
        validateSlot(slot);

        this.guiItems.put(slot, guiItem);
        this.inventory.setItem(slot, guiItem.getItemStack());
    }

    /**
     * {@inheritDoc}
     *
     * @param row {@inheritDoc}
     * @param col {@inheritDoc}
     * @param guiItem {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setItem(final int row, final int col, @NotNull final GuiItem guiItem) {
        setItem(getSlotFromRowColumn(row, col), guiItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param slots {@inheritDoc}
     * @param guiItem {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setItem(@NotNull final List<Integer> slots, @NotNull final GuiItem guiItem) {
        for (final int slot : slots) {
            setItem(slot, guiItem);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param items {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void addItem(@NotNull final GuiItem... items) {
        addItem(false, items);
    }

    /**
     * {@inheritDoc}
     *
     * @param expandIfFull {@inheritDoc}
     * @param items {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void addItem(final boolean expandIfFull, @NotNull final GuiItem... items) {
        final List<GuiItem> notAddedItems = new ArrayList<>();

        for (final GuiItem guiItem : items) {
            for (int slot = 0; slot < this.rows * 9; slot++) {
                if (this.guiItems.get(slot) != null) {

                    if (slot == this.rows * 9 - 1) {
                        notAddedItems.add(guiItem);
                    }

                    continue;
                }

                this.guiItems.put(slot, guiItem);

                break;
            }
        }

        if (!expandIfFull || this.rows >= 6 || notAddedItems.isEmpty() || (this.guiType != null && this.guiType != GuiType.CHEST)) {
            return;
        }

        this.rows++;
        this.updateInventories();

        this.addItem(true, notAddedItems.toArray(new GuiItem[0]));
    }

    /**
     * {@inheritDoc}
     *
     * @param itemStack {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void removeItem(final ItemStack itemStack) {
        final String key = GuiKeys.getUUID(itemStack);

        final Optional<Map.Entry<Integer, GuiItem>> entry = guiItems.entrySet()
                .stream()
                .filter(it -> {
                    final String pair = it.getValue().getUuid().toString();

                    return key.equalsIgnoreCase(pair);
                })
                .findFirst();

        entry.ifPresent(it -> {
            this.guiItems.remove(it.getKey());
            this.inventory.remove(it.getValue().getItemStack());
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param row {@inheritDoc}
     * @param col {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void removeItem(final int row, final int col) {
        removeItem(getSlotFromRowColumn(row, col));
    }

    /**
     * {@inheritDoc}
     *
     * @param guiItem {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void removeItem(final GuiItem guiItem) {
        removeItem(guiItem.getItemStack());
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void removeItem(final int slot) {
        validateSlot(slot);

        this.guiItems.remove(slot);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiItem getGuiItem(final int slot) {
        return this.guiItems.get(slot);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param slotAction {@inheritDoc}
     * @since 0.1.0
     */
    public void addSlotAction(final int slot, @Nullable final GuiAction<@NotNull InventoryClickEvent> slotAction) {
        this.slotActions.put(slot, slotAction);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getSlotAction(final int slot) {
        return this.slotActions.get(slot);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param reason {@inheritDoc}
     * @param isDelayed {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void close(final Player player, final InventoryCloseEvent.Reason reason, final boolean isDelayed) {
        if (isDelayed) {
            new FoliaScheduler(plugin, SchedulerType.global_scheduler) {
                @Override
                public void run() {
                    player.closeInventory(reason != null ? reason : InventoryCloseEvent.Reason.PLUGIN);
                }
            }.runDelayed(2L);

            return;
        }

        player.closeInventory(reason != null ? reason : InventoryCloseEvent.Reason.PLUGIN);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param isDelayed {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void close(final Player player, final boolean isDelayed) {
        close(player, null, isDelayed);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void close(final Player player) {
        close(player, true);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void updateTitle(final Player player) {
        PaperMethods.updateTitle(player, this.title);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void updateInventory(final Player player) {
        this.inventory.clear();

        populate();

        player.updateInventory();
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public void updateTitles() {
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            final InventoryHolder inventory = player.getOpenInventory().getTopInventory().getHolder(false);

            if (!(inventory instanceof BaseGui)) return;

            updateTitle(player);
        });
    }

    /**
     * {@inheritDoc}
     *
     * @since 0.1.0
     */
    @Override
    public void updateInventories() {
        final Inventory inventory = this.inventory;

        inventory.getViewers().forEach(humanEntity -> {
            if (humanEntity instanceof Player player) {
                updateInventory(player);
            }
        });
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public final boolean isUpdating() {
        return this.isUpdating;
    }

    /**
     * {@inheritDoc}
     *
     * @param updating {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setUpdating(final boolean updating) {
        this.isUpdating = updating;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getDefaultClickAction() {
        return this.defaultClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultClickAction {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setDefaultClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultClickAction) {
        this.defaultClickAction = defaultClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getDefaultTopClickAction() {
        return this.defaultTopClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param defaultTopClickAction {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setDefaultTopClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> defaultTopClickAction) {
        this.defaultTopClickAction = defaultTopClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getPlayerInventoryAction() {
        return this.playerInventoryAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param playerInventoryAction {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setPlayerInventoryAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> playerInventoryAction) {
        this.playerInventoryAction = playerInventoryAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryDragEvent> getDragAction() {
        return this.dragAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param dragAction {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setDragAction(@Nullable final GuiAction<@NotNull InventoryDragEvent> dragAction) {
        this.dragAction = dragAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryCloseEvent> getCloseGuiAction() {
        return this.closeGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param closeGuiAction {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setCloseGuiAction(@Nullable final GuiAction<@NotNull InventoryCloseEvent> closeGuiAction) {
        this.closeGuiAction = closeGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryOpenEvent> getOpenGuiAction() {
        return this.openGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param openGuiAction {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setOpenGuiAction(@Nullable final GuiAction<@NotNull InventoryOpenEvent> openGuiAction) {
        this.openGuiAction = openGuiAction;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @Nullable final GuiAction<InventoryClickEvent> getOutsideClickAction() {
        return this.outsideClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param outsideClickAction {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void setOutsideClickAction(@Nullable final GuiAction<@NotNull InventoryClickEvent> outsideClickAction) {
        this.outsideClickAction = outsideClickAction;
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param purge {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void open(final Player player, final boolean purge) {
        if (player.isSleeping()) return;

        if (purge) {
            this.inventory.clear();

            populate();
        }

        player.openInventory(this.inventory);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public void open(final Player player) {
        open(player, true);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.1.0
     */
    @Override
    public @NotNull final Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Creates a {@link GuiItem} instead of an {@link ItemStack}.
     *
     * @param itemStack {@link ItemStack}
     * @return a {@link GuiItem} with no {@link GuiAction}
     * @since 0.1.0
     */
    public @NotNull final GuiItem asGuiItem(final ItemStack itemStack) {
        return asGuiItem(itemStack, null);
    }

    /**
     * Creates a {@link GuiItem} instead of an {@link ItemStack}.
     *
     * @param itemStack {@link ItemStack}
     * @param action The {@link GuiAction} to apply to the item
     * @return A {@link GuiItem} with {@link GuiAction}
     * @since 0.1.0
     */
    public @NotNull final GuiItem asGuiItem(final ItemStack itemStack, @Nullable final GuiAction<InventoryClickEvent> action) {
        return new GuiItem(itemStack, action);
    }

    /**
     * Populates an inventory with items.
     *
     * @since 0.1.0
     */
    public void populate() {
        for (final Map.Entry<Integer, GuiItem> entry : this.guiItems.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue().getItemStack());
        }
    }

    /**
     * Gets the slot from the row and column passed.
     *
     * @param row the row
     * @param col the column
     * @return the slot needed
     * @since 0.1.0
     */
    public final int getSlotFromRowColumn(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }

    /**
     * Checks if the slot introduces is a valid slot.
     *
     * @param slot the slot to check
     * @since 0.1.0
     */
    private void validateSlot(final int slot) {
        final int limit = this.guiType.getLimit();

        if (this.guiType == GuiType.CHEST) {
            if (slot < 0 || slot >= this.rows * limit) throwInvalidSlot(slot);

            return;
        }

        if (slot < 0 || slot > limit) throwInvalidSlot(slot);
    }

    /**
     * Throws an exception if the slot is invalid.
     *
     * @param slot the specific slot to display in the error message
     * @since 0.1.0
     */
    private void throwInvalidSlot(final int slot) {
        if (this.guiType == GuiType.CHEST) {
            throw new GenericException("Slot " + slot + " is not valid for the gui type - " + this.guiType.name() + " and rows - " + this.rows + "!");
        }

        throw new GenericException("Slot " + slot + " is not valid for the gui type - " + this.guiType.name() + "!");
    }
}