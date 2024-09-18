package com.ryderbelserion.vital.paper.api.enums;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * An enum of plugins supported.
 *
 * @author ryderbelserion
 * @version 0.0.5
 * @since 0.0.1
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
    cmi("CMI"),
    /**
     * FancyHolograms Plugin.
     */
    fancy_holograms("FancyHolograms"),
    /**
     * DecentHolograms Plugin.
     */
    decent_holograms("DecentHolograms"),
    /**
     * FactionsUUID Plugin.
     */
    factions_uuid("FactionsUUID"),
    /**
     * Vault Plugin.
     */
    vault("Vault"),
    /**
     * Yardwatch Plugin.
     */
    yard_watch("YardWatch"),
    /**
     * WorldGuard Plugin.
     */
    world_guard("WorldGuard"),
    /**
     * McMMO Plugin.
     */
    mcmmo("McMMO"),
    /**
     * PlaceholderAPI Plugin.
     */
    placeholder_api("PlaceholderAPI");

    private final String name;

    /**
     * @param name the name of the plugin
     * @since 0.0.1
     */
    Support(@NotNull final String name) {
        this.name = name;
    }

    /**
     * Gets the enabled status of the plugin.
     * 
     * @return true or false
     * @since 0.0.1
     */
    public final boolean isEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(this.name);
    }

    /**
     * Gets the name of the plugin.
     * 
     * @return the name of the plugin
     * @since 0.0.1
     */
    public @NotNull final String getName() {
        return this.name;
    }
}