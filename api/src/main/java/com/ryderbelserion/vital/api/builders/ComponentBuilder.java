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
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public class ComponentBuilder {

    private final Vital vital = VitalProvider.get();
    private final TextComponent.@NotNull Builder builder = Component.text();
    private final Audience target;
    private String value;

    /**
     * Constructs a {@link ComponentBuilder} for the specified {@link Audience}.
     *
     * @param target the {@link Audience} to send the component to
     * @since 0.1.0
     */
    public ComponentBuilder(@NotNull final Audience target) {
        this.target = target;
    }

    /**
     * Appends a new {@link Component} to the builder.
     *
     * @param component the {@link Component} to append
     * @return the {@link ComponentBuilder} instance
     * @since 0.1.0
     */
    public @NotNull ComponentBuilder append(@NotNull final Component component) {
        this.builder.append(component);

        return this;
    }

    /**
     * Adds a hover event to the builder.
     *
     * @param text the text shown on hover
     * @return the {@link ComponentBuilder} instance
     * @since 0.1.0
     */
    public @NotNull ComponentBuilder addHoverEvent(@NotNull final String text) {
        if (!text.isEmpty()) {
            this.builder.hoverEvent(HoverEvent.showText(Methods.parse(text)));
        }

        return this;
    }

    /**
     * Adds a click event to the builder.
     *
     * @param action the click action
     * @param text the text sent on click
     * @return the {@link ComponentBuilder} instance
     * @since 0.1.0
     */
    public @NotNull ComponentBuilder addClickEvent(@Nullable final ClickEvent.Action action, @NotNull final String text) {
        if (action != null && !text.isEmpty()) {
            this.builder.clickEvent(ClickEvent.clickEvent(action, text));
        }

        return this;
    }

    /**
     * Builds the {@link TextComponent}.
     *
     * @return the built {@link TextComponent}
     * @since 0.1.0
     */
    public @NotNull TextComponent build() {
        if (this.value.isEmpty()) {
            return Component.empty();
        }

        return this.builder.append(this.vital.color(this.target, this.value, new HashMap<>())).build();
    }

    /**
     * Sends the built {@link Component} to the target.
     *
     * @since 0.1.0
     */
    public void send() {
        final Component component = build();

        if (!component.equals(Component.empty())) {
            this.target.sendMessage(component);
        }
    }

    /**
     * Gets the target audience.
     *
     * @return the target {@link Audience}
     * @since 0.1.0
     */
    public @NotNull Audience getTarget() {
        return this.target;
    }

    /**
     * Sets the value to be parsed and sent.
     *
     * @param value the value to set
     * @since 0.1.0
     */
    public void setValue(@NotNull final String value) {
        if (!value.isEmpty()) {
            this.value = value;
        }
    }

    /**
     * Gets the value to be sent to the recipient.
     *
     * @return the value
     * @since 0.1.0
     */
    public @NotNull String getValue() {
        return this.value;
    }
}