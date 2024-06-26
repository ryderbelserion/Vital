package com.ryderbelserion.vital.discord.commands

import com.ryderbelserion.vital.discord.commands.interfaces.CommandActor
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionMapping

public class CommandContext(private val event: SlashCommandInteractionEvent) : CommandActor {

    /**
     * Sends a simple string with an ephemeral toggle.
     */
    public override fun reply(message: String, ephemeral: Boolean) {
        this.event.reply(message).setEphemeral(ephemeral).queue()
    }

    /**
     * Sends an embed with an ephemeral toggle.
     */
    public override fun reply(message: MessageEmbed, ephemeral: Boolean) {
        this.event.replyEmbeds(message).setEphemeral(ephemeral).queue()
    }

    /**
     * Defer a reply, Sends the thinking... message to a user.
     *
     * @return the object
     */
    override fun defer(ephemeral: Boolean): CommandContext {
        this.event.deferReply(ephemeral).queue()

        return this
    }

    /**
     * Gets the option from the event.
     */
    public override fun getOption(option: String): OptionMapping? {
        return this.event.getOption(option)
    }

    /**
     * @author aythor of the message
     */
    public override fun author(): User {
        return this.event.user
    }

    /**
     * @return the one who created the bot.
     */
    public override fun creator(): User? {
        return jda().getUserById("209853986646261762")
    }

    /**
     * @return the bot.
     */
    public override fun bot(): SelfUser {
        return jda().selfUser
    }

    /**
     * @return guild the command is executed in.
     */
    public override fun guild(): Guild? {
        return this.event.guild
    }

    /**
     * @return jda instance.
     */
    public override fun jda(): JDA {
        return this.event.jda
    }
}