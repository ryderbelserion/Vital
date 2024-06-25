package com.ryderbelserion.vital.discord.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed.Field

public class Fields(private val embed: EmbedBuilder) {

    /**
     * Adds a field using Strings.
     *
     * @param title the title of the embed.
     * @param body the text for the field description.
     * @param inline whether the field should be inline.
     */
    public fun field(title: String, body: String, inline: Boolean = false) {
        this.embed.addField(title, body, inline)
    }

    /**
     * Adds a field based on the field object.
     *
     * @param field the field object containing all the information we need.
     * @param inline whether the field should be inline.
     */
    public fun field(field: Field, inline: Boolean = false) {
        this.embed.addField(field.name!!, field.value!!, inline)
    }

    /**
     * Adds a blank field.
     *
     * @param value whether the field should be inline.
     */
    public fun empty(value: Boolean = false) {
        this.embed.addBlankField(value)
    }
}