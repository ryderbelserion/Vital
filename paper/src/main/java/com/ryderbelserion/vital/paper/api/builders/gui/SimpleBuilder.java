package com.ryderbelserion.vital.paper.api.builders.gui;

import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.Gui;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiType;
import org.jetbrains.annotations.NotNull;
import java.util.function.Consumer;

/**
 * Base simple builder.
 *
 * @author Matt
 * @version 0.0.4
 * @since 0.0.1
 */
public final class SimpleBuilder extends BaseGuiBuilder<Gui, SimpleBuilder> {

    private GuiType guiType;

    /**
     * Main constructor to provide a way to create {@link SimpleBuilder}.
     *
     * @param guiType the {@link GuiType} to default to
     * @since 0.0.1
     */
    public SimpleBuilder(@NotNull final GuiType guiType) {
        this.guiType = guiType;
    }

    /**
     * Creates a new {@link Gui}.
     *
     * @return a new {@link Gui}
     * @since 0.0.1
     */
    @Override
    public @NotNull Gui create() {
        final Gui gui;

        gui = this.guiType == null || this.guiType == GuiType.CHEST ? new Gui(getTitle(), getRows(), getInteractionComponents()) : new Gui(getTitle(), this.guiType, getInteractionComponents());

        final Consumer<Gui> consumer = getConsumer();
        if (consumer != null) consumer.accept(gui);

        return gui;
    }

    /**
     * Sets the {@link GuiType} to use on the gui.
     * This method is unique to the simple gui.
     *
     * @param guiType the {@link GuiType}
     * @return the current builder
     * @since 0.0.1
     */
    public @NotNull SimpleBuilder setType(final GuiType guiType) {
        this.guiType = guiType;

        return this;
    }
}