package com.ryderbelserion.vital.discord.commands.interfaces

import com.ryderbelserion.vital.discord.commands.CommandEngine
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

public interface CommandFlow {

    public fun addCommand(engine: CommandEngine)

    public fun addCommand(engine: CommandEngine, optionData: OptionData)

    public fun addCommand(engine: CommandEngine, optionData: List<OptionData>)

    public fun addCommand(engine: CommandEngine, type: OptionType, name: String, description: String)

    public fun addGuildCommand(engine: CommandEngine)

    public fun addGuildCommand(engine: CommandEngine, optionData: OptionData)

    public fun addGuildCommand(engine: CommandEngine, optionData: List<OptionData>)

    public fun addGuildCommand(engine: CommandEngine, type: OptionType, name: String, description: String)

    public fun addGuildCommands(vararg engine: CommandEngine)

    public fun addCommands(vararg engine: CommandEngine)

    public fun purgeGuildCommands()

    public fun purgeGlobalCommands()

}