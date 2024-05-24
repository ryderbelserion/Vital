package com.ryderbelserion.vital.paper.enums;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * An enum of plugins supported.
 *
 * @author Ryder Belserion
 * @version 1.4
 * @since 1.0
 */
public enum Support {

    oraxen("Oraxen"),
    items_adder("ItemsAdder"),
    head_database("HeadDatabase"),
    fancy_holograms("FancyHolograms"),
    decent_holograms("DecentHolograms"),
    worldguard("WorldGuard"),
    worldedit("WorldEdit"),
    cmi("CMI"),
    placeholder_api("PlaceholderAPI");

    private final String name;

    /**
     * @param name the name of the plugin.
     * @since 1.0
     */
    Support(@NotNull final String name) {
        this.name = name;
    }

    /**
     * @return true or false.
     * @since 1.0
     */
    public final boolean isEnabled() {
        return Bukkit.getServer().getPluginManager().isPluginEnabled(this.name);
    }

    /**
     * @return the name of the plugin.
     * @since 1.0
     */
    public @NotNull final String getName() {
        return this.name;
    }
}