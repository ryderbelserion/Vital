package com.ryderbelserion.vital.discord.commands;

import com.ryderbelserion.vital.discord.commands.interfaces.CommandFlow;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

public class CommandHandler implements CommandFlow {

    private final Guild guild;
    private final JDA jda;

    public CommandHandler(final Guild guild) {
        this.guild = guild;
        this.jda = this.guild.getJDA();
    }

    @Override
    public void addCommand(final CommandEngine engine) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).queue();
    }

    @Override
    public void addCommand(final CommandEngine engine, final OptionData optionData) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();;
    }

    @Override
    public void addCommand(final CommandEngine engine, final List<OptionData> optionData) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();
    }

    @Override
    public void addCommand(final CommandEngine engine, final OptionType type, final String name, final String description) {
        this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOption(type, name, description).queue();
    }

    @Override
    public void addGuildCommand(final CommandEngine engine) {
        this.guild.upsertCommand(engine.getName(), engine.getDescription()).queue();
    }

    @Override
    public void addGuildCommand(final CommandEngine engine, final OptionData optionData) {
        this.guild.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();
    }

    @Override
    public void addGuildCommand(final CommandEngine engine, final List<OptionData> optionData) {
        this.guild.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();
    }

    @Override
    public void addGuildCommand(final CommandEngine engine, final OptionType type, final String name, final String description) {
        this.guild.upsertCommand(engine.getName(), engine.getDescription()).addOption(type, name, description).queue();
    }

    @Override
    public void addGuildCommands(final List<CommandEngine> commands) {
        commands.forEach(this::addGuildCommand);
    }

    @Override
    public void addCommands(final List<CommandEngine> commands) {
        commands.forEach(this::addCommand);
    }

    @Override
    public void purgeGuildCommands() {
        this.guild.updateCommands().queue();
    }

    @Override
    public void purgeGlobalCommands() {
        this.jda.updateCommands().queue();
    }
}
