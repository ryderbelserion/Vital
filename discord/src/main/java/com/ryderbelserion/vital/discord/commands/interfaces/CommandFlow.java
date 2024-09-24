package com.ryderbelserion.vital.discord.commands.interfaces;

import com.ryderbelserion.vital.discord.commands.CommandEngine;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

/**
 * The command flow
 *
 * @author ryderbelserion
 * @version 0.0.2
 * @since 0.0.1
 */
public interface CommandFlow {

    /**
     * Adds a global command.
     *
     * @param engine {@link CommandEngine}
     * @since 0.0.1
     */
    void addCommand(final CommandEngine engine);

    /**
     * Adds a global command.
     *
     * @param engine {@link CommandEngine}
     * @param optionData {@link OptionData}
     * @since 0.0.1
     */
    void addCommand(final CommandEngine engine, final OptionData optionData);

    /**
     * Adds a global command.
     *
     * @param engine {@link CommandEngine}
     * @param optionData a list of {@link OptionData}
     * @since 0.0.1
     */
    void addCommand(final CommandEngine engine, final List<OptionData> optionData);

    /**
     * Adds a global command.
     *
     * @param engine {@link CommandEngine}
     * @param type {@link OptionType}
     * @param name the name of the command
     * @param description the description of the command
     * @since 0.0.1
     */
    void addCommand(final CommandEngine engine, final OptionType type, final String name, final String description);

    /**
     * Removes a global command
     *
     * @param engine {@link CommandEngine}
     * @since 0.0.1
     */
    void removeCommand(final CommandEngine engine);

    /**
     * Adds a guild command.
     *
     * @param engine {@link CommandEngine}
     * @since 0.0.1
     */
    void addGuildCommand(final CommandEngine engine);

    /**
     * Adds a guild command.
     *
     * @param engine {@link CommandEngine}
     * @param optionData {@link OptionData}
     * @since 0.0.1
     */
    void addGuildCommand(final CommandEngine engine, final OptionData optionData);

    /**
     * Adds a guild command
     *
     * @param engine {@link CommandEngine}
     * @param optionData list of {@link OptionData}
     * @since 0.0.1
     */
    void addGuildCommand(final CommandEngine engine, final List<OptionData> optionData);

    /**
     * Adds a guild command.
     *
     * @param engine {@link CommandEngine}
     * @param type {@link OptionType}
     * @param name name of command
     * @param description description of command
     * @since 0.0.1
     */
    void addGuildCommand(final CommandEngine engine, final OptionType type, final String name, final String description);

    /**
     * Adds a list of guild commands.
     *
     * @param commands list of {@link CommandEngine}
     * @since 0.0.1
     */
    void addGuildCommands(final List<CommandEngine> commands);

    /**
     * Adds a list of global commands.
     *
     * @param commands list of {@link CommandEngine}
     * @since 0.0.1
     */
    void addCommands(final List<CommandEngine> commands);

    /**
     * Checks if a command exists.
     *
     * @param command the command
     * @return true or false
     * @since 0.0.1
     */
    boolean hasCommand(final String command);

    /**
     * Purge guild commands
     *
     * @since 0.0.1
     */
    void purgeGuildCommands();

    /**
     * Purge global commands
     *
     * @since 0.0.1
     */
    void purgeGlobalCommands();

}