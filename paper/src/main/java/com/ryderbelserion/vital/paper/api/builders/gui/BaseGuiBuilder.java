package com.ryderbelserion.vital.paper.api.builders.gui;

import com.ryderbelserion.vital.paper.api.catches.GenericException;
import com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.vital.paper.api.builders.gui.types.BaseGui;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Base gui builder.
 *
 * @param <B> base builder
 * @param <G> gui type
 *
 * @author Matt
 * @version 0.0.7
 * @since 0.0.1
 */
@SuppressWarnings("unchecked")
public abstract class BaseGuiBuilder<G extends BaseGui, B extends BaseGuiBuilder<G, B>> {

    private final EnumSet<InteractionComponent> components = EnumSet.noneOf(InteractionComponent.class);
    private String title = null;
    private int rows = 1;

    private Consumer<G> consumer;

    /**
     * Main constructor to provide a way to create {@link BaseGuiBuilder}
     *
     * @since 0.0.1
     */
    public BaseGuiBuilder() {}

    /**
     * Creates the given {@link BaseGui}.
     * Has to be abstract because each gui is different.
     *
     * @return the new {@link BaseGui}
     * @since 0.0.1
     */
    public abstract @NotNull G create();

    /**
     * Sets the rows for the gui.
     * This will only work on CHEST {@link GuiType}.
     *
     * @param rows the amount of rows
     * @return the builder
     * @since 0.0.1
     */
    public @NotNull final B setRows(final int rows) {
        this.rows = rows;

        return (B) this;
    }

    /**
     * Sets the title for the gui.
     * This will be either a {@link Component} or a {@link String}.
     *
     * @param title the gui title
     * @return the builder
     * @since 0.0.1
     */
    public @NotNull final B setTitle(@NotNull final String title) {
        this.title = title;

        return (B) this;
    }

    /**
     * Disables item placement in the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B disableItemPlacement() {
        this.components.add(InteractionComponent.PREVENT_ITEM_PLACE);


        return (B) this;
    }

    /**
     * Disables items to be taken in inventories.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B disableItemTake() {
        this.components.add(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    /**
     * Disables items to be swapped in the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B disableItemSwap() {
        this.components.add(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    /**
     * Disables item drops from inside the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B disableItemDrop() {
        this.components.add(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    /**
     * Disables all interactions in the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B disableInteractions() {
        this.components.addAll(InteractionComponent.VALUES);

        return (B) this;
    }

    /**
     * Enables all interactions in the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B enableInteractions() {
        this.components.clear();

        return (B) this;
    }

    /**
     * Enables item placement in the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B enableItemPlacement() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_PLACE);


        return (B) this;
    }

    /**
     * Enables items to be taken in inventories.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B enableItemTake() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_TAKE);

        return (B) this;
    }

    /**
     * Enables items to be swapped in the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B enableItemSwap() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_SWAP);

        return (B) this;
    }

    /**
     * Enables item drops from inside the gui.
     *
     * @return {@link B}
     * @since 0.0.1
     */
    public final B enableItemDrop() {
        this.components.remove(InteractionComponent.PREVENT_ITEM_DROP);

        return (B) this;
    }

    /**
     * Applies anything to the gui once it's created,
     * Can be pretty useful for setting up small things like default actions.
     *
     * @param consumer a {@link Consumer} that passes the built gui
     * @return the builder
     * @since 0.0.1
     */
    public @NotNull final B apply(@NotNull final Consumer<G> consumer) {
        this.consumer = consumer;

        return (B) this;
    }

    /**
     * Getter for the set of interaction modifiers.
     *
     * @return the set of {@link InteractionComponent}
     * @author SecretX
     * @since 0.0.1
     */
    protected @NotNull final Set<InteractionComponent> getInteractionComponents() {
        return this.components;
    }

    /**
     * Getter for the consumer.
     *
     * @return the consumer
     * @since 0.0.1
     */
    protected @Nullable final Consumer<G> getConsumer() {
        return this.consumer;
    }

    /**
     * Getter for the title.
     *
     * @return the current title
     * @since 0.0.1
     */
    protected @NotNull final String getTitle() {
        if (this.title == null) {
            throw new GenericException("The gui title is missing!");
        }

        return this.title;
    }

    /**
     * Getter for the rows.
     *
     * @return the amount of rows
     * @since 0.0.1
     */
    protected final int getRows() {
        return this.rows;
    }
}