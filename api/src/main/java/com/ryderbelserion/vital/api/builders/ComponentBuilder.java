package com.ryderbelserion.vital.api.builders;

import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.utils.Methods;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;

/**
 * Builds a component to send to a player.
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public class ComponentBuilder {

    private final Vital vital = VitalProvider.get();

    private final TextComponent.@NotNull Builder builder = Component.text();

    private final Audience target;

    private String value;

    /**
     * A constructor to build a {@link Component}.
     *
     * @param target the {@link Audience} to send to
     * @since 0.0.1
     */
    public ComponentBuilder(@NotNull final Audience target) {
        this.target = target;
    }

    /**
     * Appends a new {@link Component} to the {@link TextComponent.Builder}.
     *
     * @param component the {@link Component} to append
     * @return {@link ComponentBuilder}
     * @since 0.0.1
     */
    public @NotNull final ComponentBuilder append(@NotNull final Component component) {
        this.builder.append(component);

        return this;
    }

    /**
     * Appends a new {@link HoverEvent} to the {@link TextComponent.Builder}.
     *
     * @param text the {@link String} shown on hover.
     * @return {@link ComponentBuilder}
     * @since 0.0.1
     */
    public @NotNull final ComponentBuilder applyHover(@NotNull final String text) {
        if (text.isEmpty()) return this;

        this.builder.hoverEvent(HoverEvent.showText(Methods.parse(text)));

        return this;
    }

    /**
     * Adds a {@link ClickEvent.Action} to the {@link TextComponent.Builder}.
     *
     * @param action the {@link ClickEvent.Action} to use
     * @param text the {@link String} that is sent on click
     * @return {@link ComponentBuilder}
     * @since 0.0.1
     */
    public @NotNull final ComponentBuilder applyClick(@Nullable final ClickEvent.Action action, @NotNull final String text) {
        if (action == null || text.isEmpty()) return this;

        this.builder.clickEvent(ClickEvent.clickEvent(action, text));

        return this;
    }

    /**
     * Parses a {@link Component} and returns it.
     *
     * @return the {@link Component}
     * @since 0.0.1
     */
    public @NotNull final TextComponent getComponent() {
        if (this.value.isEmpty()) return Component.empty();

        return this.builder.append(this.vital.color(this.target, this.value, new HashMap<>())).build();
    }

    /**
     * Send a {@link Component} to someone.
     *
     * @since 0.0.1
     */
    public void send() {
        final Component component = getComponent();

        if (component.equals(Component.empty())) return;

        this.target.sendMessage(component);
    }

    /**
     * Gets the target recipient of the {@link Component}.
     *
     * @return the {@link Audience}
     * @since 0.0.1
     */
    public @NotNull final Audience getTarget() {
        return this.target;
    }

    /**
     * Sets the {@link String} to the new value.
     *
     * @param value the {@link String} to use
     * @since 0.0.1
     */
    public void applyValue(@NotNull final String value) {
        if (value.isEmpty()) return;

        this.value = value;
    }

    /**
     * Gets the {@link String} to send to the recipient.
     *
     * @return the {@link String}
     * @since 0.0.1
     */
    public @NotNull final String getValue() {
        return this.value;
    }
}