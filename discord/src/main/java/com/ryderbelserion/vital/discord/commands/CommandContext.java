package com.ryderbelserion.vital.discord.commands;

import com.ryderbelserion.vital.discord.commands.interfaces.CommandActor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.Nullable;

public class CommandContext implements CommandActor {

    private SlashCommandInteractionEvent slash;
    private MessageReceivedEvent message;

    public CommandContext(@Nullable final SlashCommandInteractionEvent slash, @Nullable final MessageReceivedEvent message) {
        if (message != null) {
            this.message = message;
        }

        if (slash != null) {
            this.slash = slash;
        }
    }

    public final boolean isSlashActive() {
        return this.slash != null;
    }

    public final boolean isMessageActive() {
        return this.message == null;
    }

    @Override
    public void reply(final String message, final boolean ephemeral) {
        if (isSlashActive()) {
            this.slash.reply(message).setEphemeral(ephemeral).queue();

            return;
        }

        this.message.getChannel().sendMessage(message).queue();
    }

    @Override
    public void reply(final MessageEmbed message, final boolean ephemeral) {
        if (isSlashActive()) {
            this.slash.replyEmbeds(message).setEphemeral(ephemeral).queue();

            return;
        }

        this.message.getChannel().sendMessageEmbeds(message).queue();
    }

    @Override
    public final CommandContext defer(final boolean ephemeral) {
        if (isSlashActive()) {
            this.slash.deferReply(ephemeral).queue();

            return this;
        }

        return this;
    }

    @Override
    public @Nullable final OptionMapping getOption(final String option) {
        return isSlashActive() ? this.slash.getOption(option) : null;
    }

    @Override
    public final User getAuthor() {
        return isSlashActive() ? this.slash.getUser() : this.message.getAuthor();
    }

    @Override
    public final User getCreator(final long id) {
        return getJDA().getUserById(id);
    }

    @Override
    public final SelfUser getBot() {
        return getJDA().getSelfUser();
    }

    @Override
    public final Guild getGuild() {
        return isSlashActive() ? this.slash.getGuild() : this.message.getGuild();
    }

    @Override
    public final JDA getJDA() {
        return isSlashActive() ? this.slash.getJDA() : this.message.getJDA();
    }
}