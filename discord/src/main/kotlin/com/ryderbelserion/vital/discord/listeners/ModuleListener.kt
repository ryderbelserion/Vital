package com.ryderbelserion.vital.discord.listeners

import com.ryderbelserion.vital.discord.VitalDiscord
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.events.session.ShutdownEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

public class ModuleListener(private val vital: VitalDiscord) : ListenerAdapter() {

    override fun onGuildReady(event: GuildReadyEvent) {
        this.vital.ready(event.guild)
    }

    override fun onShutdown(event: ShutdownEvent) {
        this.vital.stop()
    }

    override fun onReady(event: ReadyEvent) {
        this.vital.ready()
    }
}