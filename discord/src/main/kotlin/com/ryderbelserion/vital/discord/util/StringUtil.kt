package com.ryderbelserion.vital.discord.util

import com.ryderbelserion.vital.discord.embeds.Embed
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member

public object StringUtil {

    /**
     * Send a message
     */
    public fun sendMessage(message: String, id: Long, guild: Guild) {
        if (message.isEmpty() || message.isBlank()) return

        val channel = guild.getTextChannelById(id) ?: return

        channel.sendMessage(message).queue()
    }

    /**
     * Send a message
     */
    public fun sendMessage(
        author: Member,
        title: String,
        description: String?,
        color: String,
        id: Long,
        guild: Guild
    ) {
        val embed = Embed()

        embed.author(title, author.avatarUrl)

        if (description != null) {
            embed.description(description)
        }

        color(color, embed, id, guild)
    }

    /**
     * Send a message
     */
    public fun sendMessage(description: String, color: String, id: Long, guild: Guild) {
        val embed = Embed()

        embed.description(description)

        color(color, embed, id, guild)
    }

    /**
     * Send a message
     */
    private fun color(color: String, embed: Embed, id: Long, guild: Guild) {
        embed.color(color)

        val messageEmbed = embed.build()
        val channel = guild.getTextChannelById(id) ?: return

        channel.sendMessageEmbeds(messageEmbed).queue()
    }
}