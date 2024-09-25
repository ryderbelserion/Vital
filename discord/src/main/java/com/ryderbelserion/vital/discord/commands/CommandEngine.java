package com.ryderbelserion.vital.discord.commands;

import com.ryderbelserion.vital.discord.utils.MsgUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.LinkedList;
import java.util.List;

/**
 * The command engine
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public abstract class CommandEngine extends ListenerAdapter {

    private final boolean isSlashCommand;
    private final Permission permission;
    private final String description;
    private final String name;

    /**
     * Builds a command.
     *
     * @param name name of command
     * @param description description of command
     * @param permission {@link Permission}
     * @param isSlashCommand true or false
     * @since 0.0.1
     */
    public CommandEngine(final String name, final String description, final Permission permission, final boolean isSlashCommand) {
        this.isSlashCommand = isSlashCommand;
        this.description = description;
        this.permission = permission;
        this.name = name;
    }

    private final LinkedList<CommandEngine> subCommands = new LinkedList<>();

    /**
     * Executes an action when a command is run.
     *
     * @param context {@link CommandContext}
     * @since 0.0.1
     */
    protected abstract void perform(final CommandContext context);

    /**
     * Listens for slash commands
     *
     * @param event {@link SlashCommandInteractionEvent}
     * @since 0.0.1
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!this.isSlashCommand || !event.getName().equals(this.name) || this.name.isBlank()) return;

        CommandContext context = new CommandContext(event);

        perform(context);
    }

    /**
     * Listens to the message event.
     *
     * @param event {@link MessageReceivedEvent}
     * @since 0.0.1
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (this.isSlashCommand) return;

        final Message message = event.getMessage();

        //if (!message.getContentRaw().startsWith(CommandHandler.getCommandPrefix() + this.name)) return;

        final CommandContext context = new CommandContext(event, List.of(MsgUtil.getArguments(message.getContentRaw())));

        if (!context.checkRequirements(this.permission, false)) return;

        perform(context);
    }

    /**
     * Adds sub command.
     *
     * @param command {@link CommandEngine}
     * @since 0.0.1
     */
    public void addCommand(final CommandEngine command) {
        if (hasCommand(command)) return;

        this.subCommands.add(command);
    }

    /**
     * Removes sub command.
     *
     * @param command {@link CommandEngine}
     * @since 0.0.1
     */
    public void removeCommand(final CommandEngine command) {
        this.subCommands.remove(command);
    }

    /**
     * Checks if a command exists.
     *
     * @param command {@link CommandEngine}
     * @return true or false
     * @since 0.0.1
     */
    public boolean hasCommand(final CommandEngine command) {
        return this.subCommands.contains(command);
    }

    /**
     * The name of the command
     *
     * @return the name
     * @since 0.0.1
     */
    public final String getName() {
        return this.name;
    }

    /**
     * The description of the command
     *
     * @return the description
     * @since 0.0.1
     */
    public final String getDescription() {
        return this.description;
    }

    /**
     * The permission to run the command
     *
     * @return {@link Permission}
     * @since 0.0.1
     */
    public final Permission getPermission() {
        return this.permission;
    }

    /**
     * If the command is a slash command
     *
     * @return true or false
     * @since 0.0.1
     */
    public final boolean isSlashCommand() {
        return this.isSlashCommand;
    }
}