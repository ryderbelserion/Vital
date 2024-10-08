package com.ryderbelserion.vital.velocity.modules;

import com.ryderbelserion.vital.common.api.interfaces.IModule;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class handling the loading of modules.
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public class ModuleLoader {

    private final List<IModule> modules = new ArrayList<>();

    private final EventRegistry registry;

    /**
     * Creates the module loader
     *
     * @param registry {@link EventRegistry}
     */
    public ModuleLoader(@NotNull final EventRegistry registry) {
        this.registry = registry;
    }

    /**
     * Loads modules onto the server.
     */
    public void load() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.addEvent(module);
                
                module.enable();

                return;
            }

            module.disable();
        });
    }

    /**
     * Reload modules if enabled.
     */
    public void reload() {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                this.registry.addEvent(module);

                module.reload();
            } else {
                module.disable();
            }
        });
    }

    /**
     * Unregister modules if enabled.
     * 
     * @param purge clears all modules
     */
    public void unload(final boolean purge) {
        this.modules.forEach(module -> {
            if (module.isEnabled()) {
                module.disable();
            }
        });
        
        if (purge) {
            this.modules.clear();
        }
    }

    /**
     * Unregister modules if enabled.
     */
    public void unload() {
        unload(false);
    }

    /**
     * Add a module to the map.
     *
     * @param module the {@link IModule}
     */
    public void addModule(@NotNull final IModule module) {
        if (hasModule(module)) return;

        this.modules.add(module);
    }

    /**
     * Remove a module from the map.
     *
     * @param module {@link IModule}
     */
    public void removeModule(@NotNull final IModule module) {
        if (!hasModule(module)) return;

        this.modules.remove(module);
    }

    /**
     * Grab an unmodifiable list of active modules.
     *
     * @return the list of active modules.
     */
    public @NotNull final List<IModule> getModules() {
        return Collections.unmodifiableList(this.modules);
    }

    /**
     * Get the event registry instance.
     *
     * @return {@link EventRegistry}
     */
    public @NotNull final EventRegistry getRegistry() {
        return this.registry;
    }

    /**
     * Check if the module is in the modules map.
     *
     * @param module {@link IModule}
     * @return true or false
     */
    private boolean hasModule(@NotNull final IModule module) {
        final String name = module.getName();

        boolean hasModule = false;

        for (final IModule key : this.modules) {
            if (name.equals(key.getName())) {
                hasModule = true;
            }
        }

        return hasModule;
    }
}