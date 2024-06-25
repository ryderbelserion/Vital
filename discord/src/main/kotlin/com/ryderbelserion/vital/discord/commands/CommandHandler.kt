package com.ryderbelserion.vital.discord.commands

import com.ryderbelserion.vital.discord.commands.interfaces.CommandFlow
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData

public class CommandHandler : CommandFlow {

    private lateinit var jda: JDA
    private lateinit var guild: Guild

    /**
     * Sets the guild to add slash commands to.
     */
    public fun setGuild(guild: Guild): CommandHandler {
        this.guild = guild

        return this
    }

    /**
     * Sets the jda instance.
     */
    public fun setJDA(jda: JDA): CommandHandler {
        this.jda = jda

        return this
    }

    /**
     * Adds a single global slash command.
     */
    public override fun addCommand(engine: CommandEngine) {
        this.jda.upsertCommand(engine.name, engine.description).queue()
    }

    /**
     * Adds a single global slash command.
     */
    public override fun addCommand(engine: CommandEngine, type: OptionType, name: String, description: String) {
        this.jda.upsertCommand(engine.name, engine.description).addOption(type, name, description).queue()
    }

    /**
     * Adds a single global slash command.
     */
    public override fun addCommand(engine: CommandEngine, optionData: OptionData) {
        this.jda.upsertCommand(engine.name, engine.description).addOptions(optionData).queue()
    }

    /**
     * Adds multiple global slash commands.
     */
    override fun addCommand(engine: CommandEngine, optionData: List<OptionData>) {
        this.jda.upsertCommand(engine.name, engine.description).addOptions(optionData).queue()
    }

    /**
     * Adds a single slash command to guilds.
     */
    public override fun addGuildCommand(engine: CommandEngine) {
        this.guild.upsertCommand(engine.name, engine.description).queue()
    }

    /**
     * Adds a single slash command to guilds.
     */
    public override fun addGuildCommand(engine: CommandEngine, type: OptionType, name: String, description: String) {
        this.guild.upsertCommand(engine.name, engine.description).addOption(type, name, description).queue()
    }

    /**
     * Adds a single slash command to guilds.
     */
    public override fun addGuildCommand(engine: CommandEngine, optionData: OptionData) {
        this.guild.upsertCommand(engine.name, engine.description).addOptions(optionData).queue()
    }

    /**
     * Adds multiple guild slash commands.
     */
    override fun addGuildCommand(engine: CommandEngine, optionData: List<OptionData>) {
        this.guild.upsertCommand(engine.name, engine.description).addOptions(optionData).queue()
    }

    /**
     * Adds multiple slash commands to guilds.
     */
    public override fun addGuildCommands(vararg engine: CommandEngine) {
        engine.forEach { addGuildCommand(it) }
    }

    /**
     * Adds multiple global slash commands.
     */
    public override fun addCommands(vararg engine: CommandEngine) {
        engine.forEach { addCommand(it) }
    }

    /**
     * Removes all slash commands from guilds.
     */
    public override fun purgeGuildCommands() {
        this.guild.updateCommands().queue()
    }

    /**
     * Removes all global slash commands.
     */
    public override fun purgeGlobalCommands() {
        this.jda.updateCommands().queue()
    }
}