package com.ryderbelserion.vital.velocity.commands;

import com.ryderbelserion.vital.common.api.commands.Command;
import com.ryderbelserion.vital.velocity.commands.context.VelocityCommandInfo;
import com.velocitypowered.api.command.CommandSource;

/**
 * Velocity extension of Command
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class VelocityCommand extends Command<CommandSource, VelocityCommandInfo> {

    /**
     * Empty constructor for Velocity Commands
     */
    public VelocityCommand() {}

}