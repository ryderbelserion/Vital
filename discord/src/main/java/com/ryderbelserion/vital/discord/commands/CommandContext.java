package com.ryderbelserion.vital.discord.commands;

import com.ryderbelserion.vital.discord.commands.interfaces.CommandActor;
import com.ryderbelserion.vital.discord.commands.interfaces.CommandArgs;
import com.ryderbelserion.vital.discord.util.RoleUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class CommandContext implements CommandActor, CommandArgs {

    private SlashCommandInteractionEvent slash;
    private MessageReceivedEvent message;

    public CommandContext(final SlashCommandInteractionEvent slash) {
        this.slash = slash;
    }

    private List<String> args;

    public CommandContext(final MessageReceivedEvent message, final List<String> args) {
        this.message = message;

        this.args = args;
    }

    public final boolean isSlashActive() {
        return this.slash != null;
    }

    public final boolean isMessageActive() {
        return this.message == null;
    }

    public final List<String> getArgs() {
        return this.args;
    }

    public final boolean checkRequirements(final Permission permission, final boolean notifySender) {
        if (this.message == null) return true;

        final Member member = this.message.getGuild().getMemberById(getAuthor().getIdLong());

        if (member == null) return true;

        final Role role = RoleUtil.getHighestRole(member);

        if (role == null) return true;

        if (!role.hasPermission(permission)) {
            if (notifySender) {
                reply(String.format("You do not have permission to use this command! (%s)", permission.getName()));
            }

            return false;
        }

        return true;
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
    public void reply(final String message) {
        reply(message, false);
    }

    @Override
    public void reply(final MessageEmbed message) {
        reply(message, false);
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

    @Override
    public final int getArgAsInt(final int index, final String message, final boolean notifySender) {
        int value = 1;

        try {
            value = Integer.parseInt(getArgs().get(index));
        } catch (NumberFormatException exception) {
            if (!notifySender) return value;

            reply(message.replaceAll("%value%", getArgs().get(index)).replaceAll("%action%", "integer"));

            return value;
        }

        return value;
    }

    @Override
    public final float getArgAsFloat(final int index, final String message, final boolean notifySender) {
        float value = 1F;

        try {
            value = Float.parseFloat(getArgs().get(index));
        } catch (NumberFormatException exception) {
            if (!notifySender) return value;

            reply(message.replaceAll("%value%", getArgs().get(index)).replaceAll("%action%", "float"));

            return value;
        }

        return value;
    }

    @Override
    public final boolean getArgAsBoolean(final int index, final String message, final boolean notifySender) {
        String value = getArgs().get(index).toLowerCase();

        switch (value) {
            case "true", "on", "1" -> {
                return true;
            }

            case "false", "off", "0" -> {
                return false;
            }

            default -> {
                if (!notifySender) return false;

                reply(message.replaceAll("%value%", getArgs().get(index)).replaceAll("%action%", "boolean"));

                return false;
            }
        }
    }

    @Override
    public final long getArgAsLong(final int index, final String message, final boolean notifySender) {
        long value = 1L;

        try {
            value = Long.parseLong(getArgs().get(index));
        } catch (NumberFormatException exception) {
            if (!notifySender) return value;

            reply(message.replaceAll("%value%", getArgs().get(index)).replaceAll("%action%", "long"));

            return value;
        }

        return value;
    }

    @Override
    public final double getArgAsDouble(final int index, final String message, final boolean notifySender) {
        double value = 0.1;

        try {
            value = Double.parseDouble(getArgs().get(index));
        } catch (NumberFormatException exception) {
            if (!notifySender) return value;

            reply(message.replaceAll("%value%", getArgs().get(index)).replaceAll("%action%", "double"));

            return value;
        }

        return value;
    }
}