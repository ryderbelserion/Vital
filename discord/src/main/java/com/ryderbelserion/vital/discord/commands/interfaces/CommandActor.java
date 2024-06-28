package com.ryderbelserion.vital.discord.commands.interfaces;

import com.ryderbelserion.vital.discord.commands.CommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public interface CommandActor {

    void reply(final String message, final boolean ephemeral);

    void reply(final MessageEmbed message, final boolean ephemeral);

    void reply(final String message);

    void reply(final MessageEmbed message);

    CommandContext defer(final boolean ephemeral);

    OptionMapping getOption(final String option);

    User getAuthor();

    User getCreator(final long id);

    SelfUser getBot();

    Guild getGuild();

    JDA getJDA();

}