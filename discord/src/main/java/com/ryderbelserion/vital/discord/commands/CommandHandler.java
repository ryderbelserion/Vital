package com.ryderbelserion.vital.discord.commands;

import com.ryderbelserion.vital.discord.commands.interfaces.CommandFlow;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The command handler
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class CommandHandler implements CommandFlow {

    private final ConcurrentHashMap<String, CommandEngine> commands = new ConcurrentHashMap<>();

    private final JDA jda;
    private Guild guild;

    /**
     * Builds the command handler
     *
     * @param jda {@link JDA}
     * @since 0.0.1
     */
    public CommandHandler(final JDA jda) {
        this.jda = jda;
    }

    /**
     * Sets the guild
     *
     * @param guild {@link Guild}
     * @since 0.0.1
     */
    public void setGuild(final Guild guild) {
        this.guild = guild;
    }

    /**
     * Gets the {@link Guild}.
     *
     * @return {@link Guild}
     * @since 0.0.1
     */
    public final Guild getGuild() {
        return this.guild;
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@link CommandEngine}
     */
    @Override
    public void addCommand(final CommandEngine engine) {
        if (engine.isSlashCommand()) {
            this.jda.upsertCommand(engine.getName(), engine.getDescription()).queue();

            this.jda.addEventListener(engine);

            return;
        }

        if (hasCommand(engine.getName())) return;

        this.commands.put(engine.getName(), engine);

        this.jda.addEventListener(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     * @param optionData {@inheritDoc}
     */
    @Override
    public void addCommand(final CommandEngine engine, final OptionData optionData) {
        if (engine.isSlashCommand()) {
            this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();

            return;
        }

        addCommand(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     * @param optionData {@inheritDoc}
     */
    @Override
    public void addCommand(final CommandEngine engine, final List<OptionData> optionData) {
        if (engine.isSlashCommand()) {
            this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();

            return;
        }

        addCommand(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     * @param type {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     */
    @Override
    public void addCommand(final CommandEngine engine, final OptionType type, final String name, final String description) {
        if (engine.isSlashCommand()) {
            this.jda.upsertCommand(engine.getName(), engine.getDescription()).addOption(type, name, description).queue();

            return;
        }

        addCommand(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     */
    @Override
    public void removeCommand(final CommandEngine engine) {
        if (engine.isSlashCommand()) {
            this.guild.deleteCommandById(engine.getName());

            this.jda.removeEventListener(engine);

            return;
        }

        this.commands.remove(engine.getName());

        this.jda.removeEventListener(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     */
    @Override
    public void addGuildCommand(final CommandEngine engine) {
        if (engine.isSlashCommand()) {
            this.guild.upsertCommand(engine.getName(), engine.getDescription()).queue();

            return;
        }

        addCommand(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     * @param optionData {@inheritDoc}
     */
    @Override
    public void addGuildCommand(final CommandEngine engine, final OptionData optionData) {
        if (engine.isSlashCommand()) {
            this.guild.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();

            return;
        }

        addCommand(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     * @param optionData {@inheritDoc}
     */
    @Override
    public void addGuildCommand(final CommandEngine engine, final List<OptionData> optionData) {
        if (engine.isSlashCommand()) {
            this.guild.upsertCommand(engine.getName(), engine.getDescription()).addOptions(optionData).queue();

            return;
        }

        addCommand(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param engine {@inheritDoc}
     * @param type {@inheritDoc}
     * @param name {@inheritDoc}
     * @param description {@inheritDoc}
     */
    @Override
    public void addGuildCommand(final CommandEngine engine, final OptionType type, final String name, final String description) {
        if (engine.isSlashCommand()) {
            this.guild.upsertCommand(engine.getName(), engine.getDescription()).addOption(type, name, description).queue();

            return;
        }

        addCommand(engine);
    }

    /**
     * {@inheritDoc}
     *
     * @param commands {@inheritDoc}
     */
    @Override
    public void addGuildCommands(final List<CommandEngine> commands) {
        commands.forEach(this::addGuildCommand);
    }

    /**
     * {@inheritDoc}
     *
     * @param commands {@inheritDoc}
     */
    @Override
    public void addCommands(final List<CommandEngine> commands) {
        commands.forEach(this::addCommand);
    }

    /**
     * {@inheritDoc}
     *
     * @param command {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasCommand(final String command) {
        return this.commands.containsKey(command);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeGuildCommands() {
        this.guild.updateCommands().queue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void purgeGlobalCommands() {
        this.jda.updateCommands().queue();
    }
}