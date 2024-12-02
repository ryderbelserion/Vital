package com.ryderbelserion.vital.paper.api.builders.gui.interfaces;

import com.ryderbelserion.vital.paper.api.builders.gui.PaginatedBuilder;
import com.ryderbelserion.vital.paper.api.builders.gui.SimpleBuilder;
import com.ryderbelserion.vital.paper.api.builders.gui.objects.components.InteractionComponent;
import com.ryderbelserion.vital.paper.api.builders.gui.types.BaseGui;
import com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

/**
 * Creates a gui.
 *
 * @author Matt
 * @version 0.1.0
 * @since 0.1.0
 */
public class Gui extends BaseGui {

    /**
     * Main constructor for the GUI.
     *
     * @param rows the amount of rows the gui should have
     * @param title the gui's title using {@link String}
     * @param components a set containing the {@link InteractionComponent} this gui should use
     * @author SecretX
     * @since 0.1.0
     */
    public Gui(final String title, final int rows, final Set<InteractionComponent> components) {
        super(title, rows, components);
    }

    /**
     * Alternative constructor that takes both a {@link GuiType} and a set of {@link InteractionComponent}.
     *
     * @param guiType the {@link GuiType} to be used
     * @param title the gui's title using {@link String}
     * @param components a set containing the {@link InteractionComponent} this gui should use
     * @author SecretX
     * @since 0.1.0
     */
    public Gui(final String title, final GuiType guiType, final Set<InteractionComponent> components) {
        super(title, guiType, components);
    }

    /**
     * Creates a {@link SimpleBuilder} to build a {@link Gui}.
     *
     * @param type the {@link GuiType} to be used
     * @return a {@link SimpleBuilder}
     * @since 0.1.0
     */
    public static SimpleBuilder gui(@NotNull final GuiType type) {
        return new SimpleBuilder(type);
    }

    /**
     * Creates a {@link SimpleBuilder} with CHEST as the {@link GuiType}.
     *
     * @return a chest {@link SimpleBuilder}
     * @since 0.1.0
     */
    public static SimpleBuilder gui() {
        return gui(GuiType.CHEST);
    }

    /**
     * Creates a {@link PaginatedBuilder} to build a {@link PaginatedGui}.
     *
     * @return a {@link PaginatedBuilder}
     * @since 0.1.0
     */
    public static PaginatedBuilder paginated() {
        return new PaginatedBuilder();
    }
}