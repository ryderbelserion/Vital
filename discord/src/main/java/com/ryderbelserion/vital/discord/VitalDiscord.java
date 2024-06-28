package com.ryderbelserion.vital.discord;

import ch.qos.logback.classic.Logger;
import com.ryderbelserion.vital.discord.listeners.GenericListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import java.io.File;
import java.util.List;

public abstract class VitalDiscord {

    private final Logger logger;
    private final File file;
    protected final JDA jda;

    public VitalDiscord(final Logger logger, final String folder, final String token, final List<GatewayIntent> intents, final List<CacheFlag> flags) {
        this.logger = logger;

        this.file = new File(folder);

        this.jda = JDABuilder.createDefault(token, intents).enableCache(flags).addEventListeners(new GenericListener(this)).build();
    }

    public abstract void ready(final Guild guild);

    public abstract void start();

    public abstract void ready();

    public abstract void stop();

    public final File getDirectory() {
        return this.file;
    }

    public final Logger getLogger() {
        return this.logger;
    }
}