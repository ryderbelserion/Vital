package com.ryderbelserion.vital.paper;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class Vital extends JavaPlugin {

    private final VitalPaper vital;

    public Vital(final VitalPaper vital) {
        this.vital = vital;
    }

    @Override
    public void onEnable() {
        if (this.vital == null) return;

        getServer().getServicesManager().register(VitalPaper.class, this.vital, this, ServicePriority.Normal);

        if (this.vital.isVerbose()) {
            getComponentLogger().info("Successfully enabled Vital v{}", getPluginMeta().getVersion());
        }
    }

    @Override
    public void onDisable() {
        if (this.vital == null) return;

        if (this.vital.isVerbose()) {
            getComponentLogger().info("Successfully disabled Vital v{}", getPluginMeta().getVersion());
        }

        getServer().getServicesManager().unregister(VitalPaper.class, this.vital);

        this.vital.stop();
    }
}