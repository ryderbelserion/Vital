package com.ryderbelserion.vital.paper.api.enums;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * Enum representing the supported plugins.
 * Provides methods to check if a plugin is enabled and get the plugin's name.
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
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
    placeholder_api("PlaceholderAPI"),

    /**
     * LuckPerms Plugin.
     */
    luckperms("LuckPerms");

    private final String name;

    /**
     * Constructs a Support enum with the specified plugin name.
     *
     * @param name the name of the plugin
     * @since 0.1.0
     */
    Support(@NotNull final String name) {
        this.name = name;
    }

    /**
     * Checks if the plugin is enabled.
     *
     * @return true if the plugin is enabled, false otherwise
     * @since 0.1.0
     */
    public final boolean isEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(this.name);
    }

    /**
     * Gets the name of the plugin.
     *
     * @return the name of the plugin
     * @since 0.1.0
     */
    public @NotNull final String getName() {
        return this.name;
    }
}