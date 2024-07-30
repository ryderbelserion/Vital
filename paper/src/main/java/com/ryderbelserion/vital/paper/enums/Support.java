package com.ryderbelserion.vital.paper.enums;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * An enum of plugins supported.
 *
 * @author Ryder Belserion
 * @version 2.3
 * @since 1.0
 */
public enum Support {

    /**
     * Oraxen Plugin.
     */
    oraxen("Oraxen"),
    /**
     * ItemsAdder Plugin.
     */
    items_adder("ItemsAdder"),
    /**
     * HeadDatabase Plugin.
     */
    head_database("HeadDatabase"),
    /**
     * CMI Plugin.
     */
    cmi("cmi"),
    /**
     * FancyHolograms Plugin.
     */
    fancy_holograms("FancyHolograms"),
    /**
     * DecentHolograms Plugin.
     */
    decent_holograms("DecentHolograms"),
    /**
     * PlaceholderAPI Plugin.
     */
    placeholder_api("PlaceholderAPI");

    private final String name;

    /**
     * @param name the name of the plugin
     * @since 1.0
     */
    Support(@NotNull final String name) {
        this.name = name;
    }

    private final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Support.class);

    /**
     * Gets the enabled status of the plugin.
     * 
     * @return true or false
     * @since 1.0
     */
    public final boolean isEnabled() {
        return this.plugin.getServer().getPluginManager().isPluginEnabled(this.name);
    }

    /**
     * Gets the name of the plugin.
     * 
     * @return the name of the plugin
     * @since 1.0
     */
    public @NotNull final String getName() {
        return this.name;
    }
}