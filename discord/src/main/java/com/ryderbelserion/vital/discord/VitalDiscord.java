package com.ryderbelserion.vital.discord;

import com.ryderbelserion.vital.core.Vital;
import com.ryderbelserion.vital.discord.listeners.GenericListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.util.List;
import java.util.logging.Logger;

public abstract class VitalDiscord extends Vital {

    private final Logger logger;
    private final File file;
    private final JDA jda;

    public VitalDiscord(final Logger logger, final String folder, final String token, final List<GatewayIntent> intents, final List<CacheFlag> flags) {
        this.logger = logger;

        this.file = new File(folder);

        this.jda = JDABuilder.createDefault(token, intents).enableCache(flags).addEventListeners(new GenericListener(this)).build();
    }

    public abstract void ready(final Guild guild);

    public abstract void start();

    public abstract void ready();

    public abstract void stop();

    @Override
    public @NotNull final File getDirectory() {
        return this.file;
    }

    @Override
    public boolean isLogging() {
        return true;
    }

    @Override
    public @NotNull Logger getLogger() {
        return this.logger;
    }

    protected final JDA getJDA() {
        return this.jda;
    }
}