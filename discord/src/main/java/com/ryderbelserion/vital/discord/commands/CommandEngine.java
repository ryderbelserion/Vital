package com.ryderbelserion.vital.discord.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class CommandEngine extends ListenerAdapter {

    private final String name;
    private final String description;
    private final Permission permission;

    public CommandEngine(final String name, final String description, final Permission permission) {
        this.name = name;
        this.description = description;
        this.permission = permission;
    }

    protected abstract void perform(final CommandContext context);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CommandContext context = new CommandContext(event, null);

        if (!event.getName().equals(this.name)) return;

        perform(context);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        final CommandContext context = new CommandContext(null, event);

        final Message message = event.getMessage();

        if (!message.getContentRaw().startsWith(this.name)) return;

        perform(context);
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
}