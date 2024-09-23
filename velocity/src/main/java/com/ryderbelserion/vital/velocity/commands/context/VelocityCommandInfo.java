package com.ryderbelserion.vital.velocity.commands.context;

import com.mojang.brigadier.context.CommandContext;
import com.ryderbelserion.vital.common.api.commands.context.CommandInfo;
import com.velocitypowered.api.command.CommandSource;
import org.jetbrains.annotations.NotNull;

/**
 * A command data class to provide some abstraction to brigadier commands
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public class VelocityCommandInfo extends CommandInfo<CommandSource> {

    /**
     * A constructor to apply the command context
     *
     * @param context {@link CommandContext}
     */
    public VelocityCommandInfo(@NotNull final CommandContext<CommandSource> context) {
        super(context);
    }
}