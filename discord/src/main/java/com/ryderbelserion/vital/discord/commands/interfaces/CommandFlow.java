package com.ryderbelserion.vital.discord.commands.interfaces;

import com.ryderbelserion.vital.discord.commands.CommandEngine;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

public interface CommandFlow {

    void addCommand(final CommandEngine engine);

    void addCommand(final CommandEngine engine, final OptionData optionData);

    void addCommand(final CommandEngine engine, final List<OptionData> optionData);

    void addCommand(final CommandEngine engine, final OptionType type, final String name, final String description);

    void removeCommand(final CommandEngine engine);

    void addGuildCommand(final CommandEngine engine);

    void addGuildCommand(final CommandEngine engine, final OptionData optionData);

    void addGuildCommand(final CommandEngine engine, final List<OptionData> optionData);

    void addGuildCommand(final CommandEngine engine, final OptionType type, final String name, final String description);

    void addGuildCommands(final List<CommandEngine> commands);

    void addCommands(final List<CommandEngine> commands);

    boolean hasCommand(final String command);

    void purgeGuildCommands();

    void purgeGlobalCommands();

}