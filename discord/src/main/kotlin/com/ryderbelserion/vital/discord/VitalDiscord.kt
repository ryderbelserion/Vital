package com.ryderbelserion.vital.discord

import com.ryderbelserion.vital.discord.commands.CommandHandler
import com.ryderbelserion.vital.discord.listeners.ListenerBuilder
import com.ryderbelserion.vital.discord.listeners.ModuleListener
import com.ryderbelserion.vital.discord.util.scheduler.Scheduler
import dev.minn.jda.ktx.jdabuilder.default
import dev.minn.jda.ktx.jdabuilder.intents
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.utils.cache.CacheFlag
import java.io.File

public abstract class VitalDiscord(
    token: String,
    keys: List<GatewayIntent> = emptyList(),
    flags: List<CacheFlag> = emptyList(),
    private val folder: String
) {

    init {
        getDirectory().mkdir()

        Scheduler.start()
    }

    private val jda: JDA = default(token, enableCoroutines = true) {
        intents += keys

        enableCache(flags)

        addEventListeners(ModuleListener(this@VitalDiscord))
    }

    public abstract fun ready(guild: Guild)

    public abstract fun start()

    public abstract fun ready()

    public abstract fun stop()

    public fun init() {

    }

    public fun VitalDiscord.listeners(configuration: ListenerBuilder.() -> Unit): ListenerBuilder {
        return ListenerBuilder(this.jda).apply(configuration)
    }

    public fun VitalDiscord.commands(guild: Guild, configuration: CommandHandler.() -> Unit): CommandHandler {
        return CommandHandler().setJDA(this.jda).setGuild(guild).apply(configuration)
    }

    public fun VitalDiscord.commands(configuration: CommandHandler.() -> Unit): CommandHandler {
        return CommandHandler().setJDA(this.jda).apply(configuration)
    }

    private fun getDirectory(): File {
        return File(this.folder)
    }
}