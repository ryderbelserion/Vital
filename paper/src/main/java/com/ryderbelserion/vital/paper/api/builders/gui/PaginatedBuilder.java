package com.ryderbelserion.vital.paper.api.builders.gui;

import com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.Gui;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * Base paginated builder.
 *
 * @author Matt
 * @version 0.0.4
 * @since 0.0.1
 */
public class PaginatedBuilder extends BaseGuiBuilder<PaginatedGui, PaginatedBuilder> {

    private int pageSize = 0;

    /**
     * Main constructor to provide a way to create {@link PaginatedGui}.
     *
     * @since 0.0.1
     */
    public PaginatedBuilder() {}

    /**
     * Sets the desirable page size, most of the time this isn't needed.
     *
     * @param pageSize the amount of free slots that page items should occupy
     * @return {@link PaginatedBuilder}
     * @since 0.0.1
     */
    @NotNull
    @Contract("_ -> this")
    public final PaginatedBuilder pageSize(final int pageSize) {
        this.pageSize = pageSize;

        return this;
    }

    /**
     * Creates a new {@link Gui}.
     *
     * @return a new {@link Gui}
     * @since 0.0.1
     */
    @Override
    public final @NotNull PaginatedGui create() {
        final PaginatedGui gui = new PaginatedGui(getTitle(), this.pageSize, getRows(), getInteractionComponents());

        final Consumer<PaginatedGui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }
}