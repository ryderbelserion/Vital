package com.ryderbelserion.vital.discord.listeners;

import com.ryderbelserion.vital.discord.VitalDiscord;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GenericListener extends ListenerAdapter {

    private final VitalDiscord vital;

    public GenericListener(final VitalDiscord vital) {
        this.vital = vital;
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        this.vital.ready(event.getGuild());
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        this.vital.stop();
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.vital.ready();
    }
}