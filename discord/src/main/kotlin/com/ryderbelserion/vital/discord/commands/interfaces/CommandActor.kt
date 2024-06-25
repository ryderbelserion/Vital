package com.ryderbelserion.vital.discord.commands.interfaces

import com.ryderbelserion.vital.discord.commands.CommandContext
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.interactions.commands.OptionMapping

public interface CommandActor {

    public fun reply(message: String, ephemeral: Boolean)

    public fun reply(message: MessageEmbed, ephemeral: Boolean)

    public fun defer(ephemeral: Boolean): CommandContext

    public fun getOption(option: String): OptionMapping?

    public fun author(): User?

    public fun creator(): User?

    public fun bot(): SelfUser

    public fun guild(): Guild?

    public fun jda(): JDA

}