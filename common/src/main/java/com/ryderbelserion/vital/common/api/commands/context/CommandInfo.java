package com.ryderbelserion.vital.common.api.commands.context;

import com.mojang.brigadier.context.CommandContext;
import org.jetbrains.annotations.NotNull;

/**
 * An abstract command info class for each platform to extend from.
 *
 * @author ryderbelserion
 * @param <S> the command source stack
 * @version 0.0.4
 * @since 0.0.1
 */
public abstract class CommandInfo<S> {

    private final CommandContext<S> context;

    /**
     * A constructor to apply the command context
     *
     * @param context {@link CommandContext}
     */
    public CommandInfo(@NotNull final CommandContext<S> context) {
        this.context = context;
    }

    /**
     * Gets the command source stack from {@link CommandContext}
     *
     * @return {@link S}
     */
    public @NotNull final S getSource() {
        return this.context.getSource();
    }

    /**
     * Gets the argument value from the name of the argument
     *
     * @param key the name of the argument
     * @return the argument value
     */
    public @NotNull final String getStringArgument(@NotNull final String key) {
        return this.context.getArgument(key, String.class);
    }

    /**
     * Gets the argument value from the name of the argument
     *
     * @param key the name of the argument
     * @return the argument value
     */
    public final int getIntegerArgument(@NotNull final String key) {
        return this.context.getArgument(key, Integer.class);
    }

    /**
     * Gets the argument value from the name of the argument
     *
     * @param key the name of the argument
     * @return the argument value
     */
    public final float getFloatArgument(@NotNull final String key) {
        return this.context.getArgument(key, Float.class);
    }

    /**
     * Gets the argument value from the name of the argument
     *
     * @param key the name of the argument
     * @return the argument value
     */
    public final double getDoubleArgument(@NotNull final String key) {
        return this.context.getArgument(key, Double.class);
    }
}