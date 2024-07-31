package com.ryderbelserion.vital.paper.commands.modules;

import org.bukkit.event.Listener;

/**
 * An abstract class to define what a module can do.
 *
 * @author Ryder Belserion
 * @version 2.4
 * @since 2.4
 */
public abstract class ModuleHandler implements Listener {

    /**
     * Empty constructor
     */
    public ModuleHandler() {}

    /**
     * Get the name of the module.
     *
     * @return the name of the module
     */
    public abstract String getName();

    /**
     * Check if the module is enabled.
     *
     * @return true or false
     */
    public abstract boolean isEnabled();

    /**
     * Reload the module.
     */
    public abstract void reload();

    /**
     * Disables the module.
     */
    public abstract void disable();

}