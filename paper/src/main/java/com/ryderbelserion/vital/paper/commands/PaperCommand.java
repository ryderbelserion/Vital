package com.ryderbelserion.vital.paper.commands;

import com.ryderbelserion.vital.common.api.commands.Command;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;

/**
 * Paper extension of Command.
 *
 * @author ryderbelserion
 * @version 0.0.7
 * @since 0.0.1
 */
public abstract class PaperCommand extends Command<CommandSourceStack, PaperCommandInfo> {

    /**
     * Empty constructor for Paper Commands.
     *
     * @since 0.0.1
     */
    public PaperCommand() {}

}