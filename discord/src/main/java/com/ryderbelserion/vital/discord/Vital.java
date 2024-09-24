package com.ryderbelserion.vital.discord;

import ch.qos.logback.classic.Logger;
import com.ryderbelserion.vital.common.VitalAPI;
import com.ryderbelserion.vital.discord.listeners.GenericListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.List;

/**
 * A platform specific class for Discord extending {@link VitalAPI}.
 *
 * @author ryderbelserion
 * @version 0.0.2
 * @since 0.0.1
 */
public abstract class Vital implements VitalAPI {

    private final File directory;
    private final File extensions;
    private final Logger logger;

    /**
     * The jda instance
     */
    protected final JDA jda;

    /**
     * Builds the discord bot.
     *
     * @param token the token
     * @param name the name of the logger
     * @param directory the directory
     * @param extensions the extensions folder
     * @param intents the gateways
     * @param flags the flags
     * @since 0.0.1
     */
    public Vital(final String token, final String name, final String directory, final String extensions, final List<GatewayIntent> intents, final List<CacheFlag> flags) {
        this.directory = new File(directory);
        this.extensions = new File(this.directory, extensions);

        this.logger = (Logger) LoggerFactory.getLogger(name);

        this.jda = JDABuilder.createDefault(token, intents).enableCache(flags).addEventListeners(new GenericListener(this)).build();

        start();
    }

    /**
     * Runs code when the guild is ready.
     *
     * @param guild {@link Guild}
     * @since 0.0.1
     */
    public abstract void ready(final Guild guild);

    /**
     * Runs code when the bot is ready.
     * @since 0.0.1
     */
    public abstract void ready();

    /**
     * Runs code when the bot turns off.
     * @since 0.0.1
     */
    public abstract void stop();

    /**
     * Gets the {@link Logger}
     *
     * @return {@link Logger}
     * @since 0.0.1
     */
    public Logger getLogger() {
        return this.logger;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public final File getModsDirectory() {
        return this.extensions;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @since 0.0.1
     */
    @Override
    public final File getDirectory() {
        return this.directory;
    }
}