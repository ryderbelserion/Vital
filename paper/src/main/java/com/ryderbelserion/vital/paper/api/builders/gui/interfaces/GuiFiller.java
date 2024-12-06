package com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.paper.api.builders.gui.types.BaseGui;
import com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represent gui filler items.
 *
 * @author Matt
 * @version 0.1.0
 * @since 0.1.0
 */
public final class GuiFiller {

    private final BaseGui gui;

    /**
     * Creates a gui filler object.
     *
     * @param gui {@link BaseGui}
     * @since 0.1.0
     */
    public GuiFiller(final BaseGui gui) {
        this.gui = gui;
    }

    /**
     * Fills top portion of the gui.
     *
     * @param guiItem {@link GuiItem}
     * @since 0.1.0
     */
    public void fillTop(@NotNull final GuiItem guiItem) {
        fillTop(Collections.singletonList(guiItem));
    }

    /**
     * Fills top portion of the gui with alternation.
     *
     * @param guiItems list of {@link GuiItem}'s
     * @since 0.1.0
     */
    public void fillTop(@NotNull final List<GuiItem> guiItems) {
        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 0; i < 9; i++) {
            if (!this.gui.getGuiItems().containsKey(i)) this.gui.setItem(i, items.get(i));
        }
    }

    /**
     * Fills bottom portion of the gui.
     *
     * @param guiItem {@link GuiItem}
     * @since 0.1.0
     */
    public void fillBottom(@NotNull final GuiItem guiItem) {
        fillBottom(Collections.singletonList(guiItem));
    }

    /**
     * Fills bottom portion of the gui with alternation.
     *
     * @param guiItems {@link GuiItem}'s
     * @since 0.1.0
     */
    public void fillBottom(@NotNull final List<GuiItem> guiItems) {
        final int rows = this.gui.getRows();
        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 9; i > 0; i--) {
            if (this.gui.getGuiItems().get((rows * 9) - i) == null) {
                this.gui.setItem((rows * 9) - i, items.get(i));
            }
        }
    }

    /**
     * Fills the outside section of the gui with a {@link GuiItem}.
     *
     * @param guiItem {@link GuiItem}
     * @since 0.1.0
     */
    public void fillBorder(@NotNull final GuiItem guiItem) {
        fillBorder(Collections.singletonList(guiItem));
    }

    /**
     * Fill empty slots with Multiple GuiItems, goes through list and starts again.
     *
     * @param guiItems {@link GuiItem}'s
     * @since 0.1.0
     */
    public void fillBorder(@NotNull final List<GuiItem> guiItems) {
        final int rows = this.gui.getRows();
        if (rows <= 2) return;

        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 0; i < rows * 9; i++) {
            if ((i <= 8) || (i >= (rows * 9) - 8) && (i <= (rows * 9) - 2) || i % 9 == 0 || i % 9 == 8) {
                this.gui.setItem(i, items.get(i));
            }
        }
    }

    /**
     * Fills rectangle from points within the gui.
     *
     * @param rowFrom row point 1
     * @param colFrom column point 1
     * @param rowTo row point 2
     * @param colTo column point 2
     * @param guiItem {@link GuiItem} to fill with
     * @author Harolds
     * @since 0.1.0
     */
    public void fillBetweenPoints(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final GuiItem guiItem) {
        fillBetweenPoints(rowFrom, colFrom, rowTo, colTo, Collections.singletonList(guiItem));
    }

    /**
     * Fills rectangle from points within the gui.
     *
     * @param rowFrom row point 1
     * @param colFrom column point 1
     * @param rowTo row point 2
     * @param colTo column point 2
     * @param guiItems {@link GuiItem}'s to fill with
     * @author Harolds
     * @since 0.1.0
     */
    public void fillBetweenPoints(final int rowFrom, final int colFrom, final int rowTo, final int colTo, @NotNull final List<GuiItem> guiItems) {
        final int minRow = Math.min(rowFrom, rowTo);
        final int maxRow = Math.max(rowFrom, rowTo);
        final int minCol = Math.min(colFrom, colTo);
        final int maxCol = Math.max(colFrom, colTo);

        final int rows = this.gui.getRows();
        final List<GuiItem> items = repeatList(guiItems);

        for (int row = 1; row <= rows; row++) {
            for (int col = 1; col <= 9; col++) {
                final int slot = getSlotFromRowCol(row, col);

                if (!((row >= minRow && row <= maxRow) && (col >= minCol && col <= maxCol))) continue;

                this.gui.setItem(slot, items.get(slot));
            }
        }
    }

    /**
     * Sets an GuiItem to fill up the entire inventory where there is no other item.
     *
     * @param guiItem the item to use as fill
     * @since 0.1.0
     */
    public void fill(@NotNull final GuiItem guiItem) {
        fill(Collections.singletonList(guiItem));
    }

    /**
     * Fill empty slots with Multiple GuiItems, goes through list and starts again.
     *
     * @param guiItems {@link GuiItem}
     * @since 0.1.0
     */
    public void fill(@NotNull final List<GuiItem> guiItems) {
        if (this.gui instanceof PaginatedGui) {
            throw new GenericException("Full filling a GUI is not supported in a Paginated GUI!");
        }

        final GuiType type = this.gui.getGuiType();

        final int fill = type == GuiType.CHEST ? this.gui.getRows() * type.getLimit() : type.getLimit();

        final List<GuiItem> items = repeatList(guiItems);

        for (int i = 0; i < fill; i++) {
            if (this.gui.getGuiItems().get(i) == null) this.gui.setItem(i, items.get(i));
        }
    }

    /**
     * Fills specified side of the GUI with a GuiItem.
     *
     * @param side {@link Side}
     * @param guiItems {@link GuiItem}
     * @since 0.1.0
     */
    public void fillSide(@NotNull final Side side, @NotNull final List<GuiItem> guiItems) {
        switch (side) {
            case LEFT:
                this.fillBetweenPoints(1, 1, this.gui.getRows(), 1, guiItems);
            case RIGHT:
                this.fillBetweenPoints(1, 9, this.gui.getRows(), 9, guiItems);
            case BOTH:
                this.fillSide(Side.LEFT, guiItems);
                this.fillSide(Side.RIGHT, guiItems);
        }
    }

    /**
     * Repeats a list of items. Allows for alternating items.
     * Stores references to existing objects -> Does not create new objects.
     *
     * @param guiItems list of {@link GuiItem}'s to repeat
     * @return new list
     * @since 0.1.0
     */
    private List<GuiItem> repeatList(@NotNull final List<GuiItem> guiItems) {
        final List<GuiItem> repeated = new ArrayList<>();

        Collections.nCopies(this.gui.getRows() * 9, guiItems).forEach(repeated::addAll);

        return repeated;
    }

    /**
     * Gets the slot from the row and col passed.
     *
     * @param row the row row
     * @param col the column
     * @return the new slot
     * @since 0.1.0
     */
    private int getSlotFromRowCol(final int row, final int col) {
        return (col + (row - 1) * 9) - 1;
    }

    /**
     * Each side of the gui.
     *
     * @author Matt
     * @version 0.1.0
     * @since 0.1.0
     */
    public enum Side {
        /**
         * Left side of gui.
         */
        LEFT,
        /**
         * Right side of gui.
         */
        RIGHT,
        /**
         * Both sides of gui.
         */
        BOTH
    }
}