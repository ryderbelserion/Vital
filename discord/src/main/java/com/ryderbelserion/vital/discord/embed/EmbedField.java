package com.ryderbelserion.vital.discord.embed;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class EmbedField {

    private final EmbedBuilder builder;

    public EmbedField(final EmbedBuilder builder) {
        this.builder = builder;
    }

    /**
     * Adds a field using Strings.
     *
     * @param title the title of the embed
     * @param body the text for the field description
     * @param inline whether the field should be inline
     */
    public void field(final String title, final String body, final boolean inline) {
        this.builder.addField(title, body, inline);
    }

    /**
     * Adds a field based on the field object.
     *
     * @param field the field object containing all the information we need
     */
    public void field(final MessageEmbed.Field field) {
        field(field.getName(), field.getValue(), field.isInline());
    }

    /**
     * Adds a field using Strings.
     *
     * @param title the title of the embed
     * @param body the text for the field description
     */
    public void field(final String title, final String body) {
        field(title, body, false);
    }

    /**
     * Adds a blank field.
     *
     * @param blankField blank field
     */
    public void empty(final boolean blankField) {
        this.builder.addBlankField(blankField);
    }
}