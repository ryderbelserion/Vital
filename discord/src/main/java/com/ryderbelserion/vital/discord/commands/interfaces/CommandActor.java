package com.ryderbelserion.vital.discord.commands.interfaces;

import com.ryderbelserion.vital.discord.commands.CommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

/**
 * The command actor
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public interface CommandActor {

    /**
     * Reply to a user with a message.
     *
     * @param message the message
     * @param ephemeral true or false
     * @since 0.0.1
     */
    void reply(final String message, final boolean ephemeral);

    /**
     * Reply to a user with an embed.
     *
     * @param message {@link MessageEmbed}
     * @param ephemeral true or false
     * @since 0.0.1
     */
    void reply(final MessageEmbed message, final boolean ephemeral);

    /**
     * Reply to a user with a message.
     *
     * @param message the message
     * @since 0.0.1
     */
    void reply(final String message);

    /**
     * Reply to a user with an embed.
     *
     * @param message {@link MessageEmbed}
     * @since 0.0.1
     */
    void reply(final MessageEmbed message);

    /**
     * Defer a reply
     *
     * @param ephemeral true or false
     * @return {@link CommandContext}
     * @since 0.0.1
     */
    CommandContext defer(final boolean ephemeral);

    /**
     * Get an option
     *
     * @param option the option name
     * @return {@link OptionMapping}
     * @since 0.0.1
     */
    OptionMapping getOption(final String option);

    /**
     * Get the {@link User} who is the author.
     *
     * @return {@link User}
     * @since 0.0.1
     */
    User getAuthor();

    /**
     * Gets the bot creator.
     *
     * @param id the id of the bot creator
     * @return {@link User}
     * @since 0.0.1
     */
    User getCreator(final long id);

    /**
     * Gets the bot user
     *
     * @return {@link SelfUser}
     * @since 0.0.1
     */
    SelfUser getBot();

    /**
     * Gets the {@link Guild}.
     *
     * @return {@link Guild}
     * @since 0.0.1
     */
    Guild getGuild();

    /**
     * Gets the {@link JDA}.
     *
     * @return {@link JDA}
     * @since 0.0.1
     */
    JDA getJDA();

}