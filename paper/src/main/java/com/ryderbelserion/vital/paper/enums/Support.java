package com.ryderbelserion.vital.paper.enums;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * An enum of plugins supported.
 *
 * @author Ryder Belserion
 * @version 1.5
 * @since 1.0
 */
public enum Support {

    /**
     * Oraxen Plugin
     */
    oraxen("Oraxen"),
    /**
     * ItemsAdder Plugin
     */
    items_adder("ItemsAdder"),
    /**
     * HeadDatabase Plugin
     */
    head_database("HeadDatabase"),
    /**
     * FancyHolograms Plugin
     */
    fancy_holograms("FancyHolograms"),
    /**
     * DecentHolograms Plugin
     */
    decent_holograms("DecentHolograms"),
    /**
     * WorldGuard Plugin
     */
    worldguard("WorldGuard"),
    /**
     * WorldEdit Plugin
     */
    worldedit("WorldEdit"),
    /**
     * CMI Plugin
     */
    cmi("CMI"),
    /**
     * PlaceholderAPI Plugin
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

    /**
     * Gets the enabled status of the plugin.
     * 
     * @return true or false
     * @since 1.0
     */
    public final boolean isEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(this.name);
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