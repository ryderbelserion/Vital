package com.ryderbelserion.vital.paper.commands;

import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A command data class to provide some abstraction to brigadier commands
 *
 * @author Ryder Belserion
 * @version 2.4.8
 * @since 2.4
 */
public class CommandData {

    private final CommandContext<CommandSourceStack> context;

    /**
     * A constructor to apply the command context
     *
     * @param context {@link CommandContext}
     */
    public CommandData(@NotNull final CommandContext<CommandSourceStack> context) {
        this.context = context;
    }

    /**
     * Gets the command source stack from {@link CommandContext}
     *
     * @return {@link CommandSourceStack}
     */
    public @NotNull final CommandSourceStack getSource() {
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

    /**
     * Gets the {@link CommandSender} from the {@link CommandSourceStack}.
     *
     * @return {@link CommandSender}
     */
    public @NotNull final CommandSender getCommandSender() {
        return getSource().getSender();
    }

    /**
     * Gets the {@link Player} from the {@link CommandSourceStack}.
     *
     * @return {@link Player}
     */
    public @NotNull final Player getPlayer() {
        return (Player) getCommandSender();
    }

    /**
     * Checks if the {@link CommandSender} is a {@link Player}
     *
     * @return true or false
     */
    public final boolean isPlayer() {
        return getCommandSender() instanceof Player;
    }
}