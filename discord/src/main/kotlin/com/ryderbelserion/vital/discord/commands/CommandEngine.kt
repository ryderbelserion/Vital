package com.ryderbelserion.vital.discord.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

public abstract class CommandEngine(
    public val name: String,
    public val description: String,
    public val permission: Permission
) : ListenerAdapter() {

    protected abstract fun perform(context: CommandContext)

    public override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val context = CommandContext(event)

        if (!event.name.equals(this.name, ignoreCase = true)) return

        perform(context)
    }
}