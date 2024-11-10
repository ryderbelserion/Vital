package com.ryderbelserion.vital.paper.api;

import com.ryderbelserion.vital.paper.Vital;
import com.ryderbelserion.vital.paper.VitalPaper;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VitalLoader implements PluginBootstrap {

    private VitalPaper vital;

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        this.vital = new VitalPaper(context);
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        return new Vital(this.vital);
    }
}