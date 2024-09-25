package com.ryderbelserion.vital.discord.utils;

import com.ryderbelserion.vital.discord.embed.Embed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * Utilities related to sending messages.
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public class MsgUtil {

    /**
     * Utilities related to messages.
     *
     * @since 0.0.1
     */
    public MsgUtil() {
        throw new AssertionError();
    }

    /**
     * Checks if a command is a valid command.
     *
     * @param prefix the prefix
     * @param command the command
     * @return true or false
     */
    public static boolean isCommand(final String prefix, final String command) {
        return command.toLowerCase().startsWith(prefix);
    }

    /**
     * Gets the arguments from a command.
     *
     * @param command the command
     * @return the arguments
     * @since 0.0.1
     */
    public static String[] getArguments(final String command) {
        return command.substring(1).split(" ");
    }

    /**
     * Send a message to a channel
     *
     * @param message the message to send
     * @param guild the guild to send in
     * @param id channel id
     * @since 0.0.1
     */
    public static void sendMessage(final String message, final Guild guild, final long id) {
        if (message.isEmpty() || message.isBlank()) return;

        TextChannel channel = guild.getTextChannelById(id);

        if (channel == null) return;

        channel.sendMessage(message).queue();
    }

    /**
     * Send a message to a channel.
     *
     * @param member the member to send to
     * @param guild the guild to send in
     * @param title embed title
     * @param description embed description
     * @param color embed color
     * @param id channel id
     * @since 0.0.1
     */
    public static void sendMessage(final Member member,
                                   final Guild guild,
                                   final String title,
                                   final String description,
                                   final String color,
                                   final long id) {
        final Embed embed = new Embed();

        embed.author(title, member.getAvatarUrl());

        if (description != null) embed.description(description);

        color(guild, id, color, embed);
    }

    /**
     * Send a message to a channel.
     *
     * @param guild the guild to send in
     * @param id channel id
     * @param color embed color
     * @param description embed description
     * @since 0.0.1
     */
    public static void sendMessage(final Guild guild, final long id, final String color, final String description) {
        Embed embed = new Embed();

        embed.description(description);

        color(guild, id, color, embed);
    }

    /**
     * Send a message with color.
     *
     * @param guild the guild to send in
     * @param id channel id
     * @param color embed color
     * @param embed embed object
     * @since 0.0.1
     */
    private static void color(final Guild guild, final long id, final String color, final Embed embed) {
        embed.color(color);

        final MessageEmbed message = embed.build();
        final TextChannel channel = guild.getTextChannelById(id);

        if (channel == null) return;

        channel.sendMessageEmbeds(message).queue();
    }
}