package com.ryderbelserion.vital.discord.listeners;

import com.ryderbelserion.vital.discord.Vital;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * The generic listener
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class GenericListener extends ListenerAdapter {

    private final Vital vital;

    /**
     * Builds the generic listener
     *
     * @param vital {@link Vital}
     * @since 0.0.1
     */
    public GenericListener(final Vital vital) {
        this.vital = vital;
    }

    /**
     * Fires when the guild is ready
     *
     * @param event {@link GuildReadyEvent}
     * @since 0.0.1
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        this.vital.ready(event.getGuild());
    }

    /**
     * Fires when the bot shuts down
     *
     * @param event {@link ShutdownEvent}
     * @since 0.0.1
     */
    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        this.vital.stop();
    }

    /**
     * Fires when the bot is ready
     *
     * @param event {@link ReadyEvent}
     * @since 0.0.1
     */
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        this.vital.ready();
    }
}