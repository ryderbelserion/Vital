package com.ryderbelserion.vital.discord.commands;

import com.ryderbelserion.vital.discord.util.MsgUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.LinkedList;
import java.util.List;

public abstract class CommandEngine extends ListenerAdapter {

    private final boolean isSlashCommand;
    private final Permission permission;
    private final String description;
    private final String name;

    public CommandEngine(final String name, final String description, final Permission permission, final boolean isSlashCommand) {
        this.isSlashCommand = isSlashCommand;
        this.description = description;
        this.permission = permission;
        this.name = name;
    }

    private final LinkedList<CommandEngine> subCommands = new LinkedList<>();

    protected abstract void perform(final CommandContext context);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!this.isSlashCommand || !event.getName().equals(this.name) || this.name.isBlank()) return;

        CommandContext context = new CommandContext(event);

        perform(context);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (this.isSlashCommand) return;

        final Message message = event.getMessage();

        //if (!message.getContentRaw().startsWith(CommandHandler.getCommandPrefix() + this.name)) return;

        final CommandContext context = new CommandContext(event, List.of(MsgUtil.getArguments(message.getContentRaw())));

        if (!context.checkRequirements(this.permission, false)) return;

        perform(context);
    }

    public void addCommand(final CommandEngine command) {
        if (hasCommand(command)) return;

        this.subCommands.add(command);
    }

    public void removeCommand(final CommandEngine command) {
        this.subCommands.remove(command);
    }

    public boolean hasCommand(final CommandEngine command) {
        return this.subCommands.contains(command);
    }

    public final String getName() {
        return this.name;
    }

    public final String getDescription() {
        return this.description;
    }

    public final Permission getPermission() {
        return this.permission;
    }

    public final boolean isSlashCommand() {
        return this.isSlashCommand;
    }
}