package com.ryderbelserion.vital.paper.builders;

import com.ryderbelserion.vital.paper.util.MiscUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Builds a component to send to a player.
 *
 * @author Ryder Belserion
 * @version 1.4
 * @since 1.0
 */
public class ComponentBuilder {

    private final TextComponent.@NotNull Builder builder = Component.text();

    private final Player target;

    private String value;

    /**
     * A constructor to build a {@link Component}
     *
     * @param target the {@link Player} to send to
     * @since 1.0
     */
    public ComponentBuilder(@NotNull final Player target) {
        this.target = target;
    }

    /**
     * Appends a new {@link Component} to the {@link TextComponent.Builder}
     *
     * @param component the {@link Component} to append
     * @return {@link ComponentBuilder}
     * @since 1.0
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
     * @since 1.0
     */
    public @NotNull final ComponentBuilder applyHover(@NotNull final String text) {
        if (text.isEmpty()) return this;

        this.builder.hoverEvent(HoverEvent.showText(MiscUtil.parse(text)));

        return this;
    }

    /**
     * Adds a {@link ClickEvent.Action} to the {@link TextComponent.Builder}.
     *
     * @param action the {@link ClickEvent.Action} to use
     * @param text the {@link String} that is sent on click
     * @return {@link ComponentBuilder}
     * @since 1.0
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
     * @since 1.0
     */
    public @NotNull final TextComponent getComponent() {
        if (this.value.isEmpty()) return Component.empty();

        Component message = MiscUtil.parse(this.value);

        return this.builder.append(message).build();
    }

    /**
     * Send a {@link Component} to someone
     * @since 1.0
     */
    public void send() {
        if (getComponent().equals(Component.empty())) return;

        this.target.sendMessage(getComponent());
    }

    /**
     * Gets the target recipient of the {@link Component}.
     *
     * @return the {@link Player}
     * @since 1.0
     */
    public @NotNull final Player getTarget() {
        return this.target;
    }

    /**
     * Sets the {@link String} to the new value.
     *
     * @param value the {@link String} to use
     * @since 1.0
     */
    public void applyValue(@NotNull final String value) {
        if (value.isEmpty()) return;

        this.value = value;
    }

    /**
     * Gets the {@link String} to send to the recipient.
     *
     * @return the {@link String}
     * @since 1.0
     */
    public @NotNull final String getValue() {
        return this.value;
    }
}