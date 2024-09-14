package com.ryderbelserion.vital.paper.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.vital.common.api.commands.context.CommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * A command data class to provide some abstraction to brigadier commands
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class PaperCommandInfo extends CommandInfo<CommandSourceStack> {

    /**
     * A constructor to apply the command context
     *
     * @param context {@link CommandContext}
     * @since 0.0.1
     */
    public PaperCommandInfo(@NotNull final CommandContext<CommandSourceStack> context) {
        super(context);
    }

    /**
     * Gets the {@link CommandSender} from the {@link CommandSourceStack}.
     *
     * @return {@link CommandSender}
     * @since 0.0.1
     */
    public @NotNull final CommandSender getCommandSender() {
        return getSource().getSender();
    }

    /**
     * Gets the {@link Player} from the {@link CommandSourceStack}.
     *
     * @return {@link Player}
     * @since 0.0.1
     */
    public @NotNull final Player getPlayer() {
        return (Player) getCommandSender();
    }

    /**
     * Checks if the {@link CommandSender} is a {@link Player}
     *
     * @return true or false
     * @since 0.0.1
     */
    public final boolean isPlayer() {
        return getCommandSender() instanceof Player;
    }
}