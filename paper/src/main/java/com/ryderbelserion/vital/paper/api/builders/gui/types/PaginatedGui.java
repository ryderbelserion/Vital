package com.ryderbelserion.vital.paper.api.builders.gui.types;

import com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.types.IPaginatedGui;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Creates a paginated gui
 *
 * @author Matt
 * @version 0.0.6
 * @since 0.0.1
 */
public class PaginatedGui extends BaseGui implements IPaginatedGui {

    private final List<GuiItem> pageItems = new ArrayList<>();
    private final Map<Integer, GuiItem> currentPage;

    private int pageNumber = 1;
    private int pageSize;

    /**
     * Main constructor to provide a way to create {@link PaginatedGui}
     *
     * @param rows the amount of rows the GUI should have
     * @param pageSize the page size.
     * @param title the gui's title using {@link String}
     * @param components a set containing what {@link InteractionComponent} this gui should have
     * @author SecretX
     * @since 0.0.1
     */
    public PaginatedGui(@NotNull final String title, final int pageSize, final int rows, @NotNull final Set<InteractionComponent> components) {
        super(title, rows, components);

        if (pageSize == 0) {
            calculatePageSize();
        } else {
            this.pageSize = pageSize;
        }

        int size = rows * 9;

        this.currentPage = new LinkedHashMap<>(size);
    }

    /**
     * {@inheritDoc}
     *
     * @param pageSize {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final PaginatedGui setPageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void addItem(@NotNull final GuiItem guiItem) {
        this.pageItems.add(guiItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param items {@inheritDoc}
     */
    @Override
    public void addItem(@NotNull final GuiItem... items) {
        this.pageItems.addAll(Arrays.asList(items));
    }

    /**
     * {@inheritDoc}
     *
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void removePageItem(@NotNull final GuiItem guiItem) {
        this.pageItems.remove(guiItem);

        updatePage();
    }

    /**
     * {@inheritDoc}
     *
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void removePageItem(@NotNull final ItemStack itemStack) {
        final String key = GuiKeys.getUUID(itemStack);

        final Optional<GuiItem> guiItem = this.pageItems.stream().filter(it -> {
            final String pair = it.getUuid().toString();

            return key.equalsIgnoreCase(pair);
        }).findFirst();

        guiItem.ifPresent(this::removePageItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int slot, @NotNull final ItemStack itemStack) {
        if (!this.currentPage.containsKey(slot)) return;

        final GuiItem guiItem = this.currentPage.get(slot);

        guiItem.setItemStack(itemStack);

        getInventory().setItem(slot, guiItem.getItemStack());
    }

    /**
     * {@inheritDoc}
     *
     * @param row {@inheritDoc}
     * @param col {@inheritDoc}
     * @param itemStack {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int row, final int col, @NotNull final ItemStack itemStack) {
        updatePageItem(getSlotFromRowColumn(row, col), itemStack);
    }

    /**
     * {@inheritDoc}
     *
     * @param row {@inheritDoc}
     * @param col {@inheritDoc}
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int row, final int col, @NotNull final GuiItem guiItem) {
        updatePageItem(getSlotFromRowColumn(row, col), guiItem);
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @param guiItem {@inheritDoc}
     */
    @Override
    public void updatePageItem(final int slot, @NotNull final GuiItem guiItem) {
        if (!this.currentPage.containsKey(slot)) return;

        final int index = this.pageItems.indexOf(this.currentPage.get(slot));

        // Updates both lists and inventory
        this.currentPage.put(slot, guiItem);
        this.pageItems.set(index, guiItem);

        getInventory().setItem(slot, guiItem.getItemStack());
    }

    /**
     * Overrides {@link BaseGui#open(Player, boolean)} to use the paginated populator instead
     *
     * @param player {@inheritDoc}
     * @param purge true or false
     */
    @Override
    public void open(@NotNull final Player player, final boolean purge) {
        open(player, 1, null);
    }

    /**
     * Overrides {@link BaseGui#open(Player)} to use the paginated populator instead
     *
     * @param player {@inheritDoc}
     */
    @Override
    public void open(@NotNull final Player player) {
        open(player, 1, null);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param consumer {@inheritDoc}
     */
    @Override
    public void open(@NotNull final Player player, @NotNull final Consumer<PaginatedGui> consumer) {
        open(player, 1, consumer);
    }

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     * @param openPage {@inheritDoc}
     */
    @Override
    public void open(@NotNull final Player player, final int openPage, @Nullable final Consumer<PaginatedGui> consumer) {
        if (player.isSleeping()) return;

        if (openPage <= getMaxPages() || openPage > 0) this.pageNumber = openPage;

        getInventory().clear();
        this.currentPage.clear();

        populate();

        // calculate anyway, just in case.
        if (this.pageSize == 0 || this.pageSize == getSize()) calculatePageSize();

        populatePage();

        if (consumer != null) {
            consumer.accept(this);
        }

        player.openInventory(getInventory());
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getNextPageNumber() {
        if (this.pageNumber + 1 > getMaxPages()) return this.pageNumber;

        return pageNumber + 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getPreviousPageNumber() {
        if (this.pageNumber - 1 == 0) return this.pageNumber;

        return this.pageNumber - 1;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final boolean next() {
        if (this.pageNumber + 1 > getMaxPages()) return false;

        this.pageNumber++;

        updatePage();

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final boolean previous() {
        if (this.pageNumber - 1 == 0) return false;

        this.pageNumber--;

        updatePage();

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param slot {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final GuiItem getPageItem(final int slot) {
        return this.currentPage.get(slot);
    }

    /**
     * {@inheritDoc}
     *
     * @param pageNumber {@inheritDoc}
     */
    @Override
    public void setPageNumber(final int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final int getCurrentPageNumber() {
        return this.pageNumber;
    }

    /**
     * {@inheritDoc}
     *
     * @param givenPage {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public final List<GuiItem> getItemsFromPage(final int givenPage) {
        final int page = givenPage - 1;

        final List<GuiItem> guiPage = new ArrayList<>();

        int max = ((page * this.pageSize) + this.pageSize);
        if (max > this.pageItems.size()) max = this.pageItems.size();

        for (int i = page * this.pageSize; i < max; i++) {
            guiPage.add(this.pageItems.get(i));
        }

        return guiPage;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final Map<Integer, GuiItem> getCurrentPageItems() {
        return this.currentPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearPageContents() {
        for (Map.Entry<Integer, GuiItem> entry : this.currentPage.entrySet()) {
            getInventory().setItem(entry.getKey(), null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param update {@inheritDoc}
     */
    @Override
    public void clearPageItems(boolean update) {
        this.pageItems.clear();

        if (update) {
            updatePage();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearPageItems() {
        clearPageItems(false);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    public final int getMaxPages() {
        return (int) Math.ceil((double) this.pageItems.size() / this.pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void populatePage() {
        int slot = 0;
        final int inventorySize = getInventory().getSize();

        final Iterator<GuiItem> iterator = getItemsFromPage(this.pageNumber).iterator();

        while (iterator.hasNext()) {
            if (slot >= inventorySize) {
                break; // Exit the loop if slot exceeds inventory size
            }

            if (getGuiItem(slot) != null || getInventory().getItem(slot) != null) {
                slot++;

                continue;
            }

            final GuiItem guiItem = iterator.next();

            this.currentPage.put(slot, guiItem);

            getInventory().setItem(slot, guiItem.getItemStack());

            slot++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePage() {
        clearPageContents();
        populatePage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void calculatePageSize() {
        int counter = 0;

        for (int slot = 0; slot < getSize(); slot++) {
            if (getInventory().getItem(slot) == null) counter++;
        }

        this.pageSize = counter;
    }

    // Overridden methods from the BaseGui class

    /**
     * {@inheritDoc}
     *
     * @param player {@inheritDoc}
     */
    @Override
    public void updateInventory(final Player player) {
        getInventory().clear();
        populate();
    }
}