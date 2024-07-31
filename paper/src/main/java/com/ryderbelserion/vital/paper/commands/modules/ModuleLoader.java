package com.ryderbelserion.vital.paper.commands.modules;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class handling the loading of modules.
 *
 * @author Ryder Belserion
 * @version 2.4.4
 * @since 2.4
 */
public class ModuleLoader {

    private final List<ModuleHandler> modules = new ArrayList<>();

    private EventRegistry registry;

    /**
     * Empty constructor
     */
    public ModuleLoader() {};

    /**
     * Loads module listeners onto the server.
     * 
     * @param plugin {@link JavaPlugin}
     */
    public void load(@NotNull final JavaPlugin plugin) {
        if (this.registry == null) this.registry = new EventRegistry(plugin);

        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.addListener(module);
                
                module.enable();

                return;
            }

            module.disable();

            this.registry.removeListener(module);
        });
    }

    /**
     * Reload modules if enabled.
     */
    public void reload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                module.reload();
            }
        });
    }

    /**
     * Unregister a module if enabled.
     */
    public void unload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.removeListener(module);

                module.disable();
            }
        });
    }

    /**
     * Add a module to the map.
     *
     * @param module the {@link ModuleHandler}
     */
    public void addModule(final ModuleHandler module) {
        if (containsModule(module)) return;

        this.modules.add(module);
    }

    /**
     * Remove a module from the map.
     *
     * @param module {@link ModuleHandler}
     */
    public void removeModule(final ModuleHandler module) {
        if (!containsModule(module)) return;

        this.modules.remove(module);
    }

    /**
     * Grab an unmodifiable list of active modules.
     *
     * @return the list of active modules.
     */
    public final List<ModuleHandler> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    /**
     * Get the event registry instance.
     *
     * @return {@link EventRegistry}
     */
    public final EventRegistry getRegistry() {
        return this.registry;
    }

    /**
     * Check if the module is in the modules map.
     *
     * @param module {@link ModuleHandler}
     * @return true or false
     */
    private boolean containsModule(final ModuleHandler module) {
        return this.modules.contains(module);
    }
}