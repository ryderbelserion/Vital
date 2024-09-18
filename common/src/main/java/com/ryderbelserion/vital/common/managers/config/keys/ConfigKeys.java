package com.ryderbelserion.vital.common.managers.config.keys;

import ch.jalu.configme.Comment;
import ch.jalu.configme.SettingsHolder;
import ch.jalu.configme.properties.Property;
import com.ryderbelserion.vital.common.managers.config.beans.Plugin;
import static ch.jalu.configme.properties.PropertyInitializer.newBeanProperty;

/**
 * Config options for the vital.yml file
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class ConfigKeys implements SettingsHolder {

    /**
     * Config options for the vital.yml file
     *
     * @author ryderbelserion
     * @since 0.0.1
     */
    public ConfigKeys() {}

    /**
     * Configure the library bundled with the plugin.
     */
    @Comment("Configure the library bundled with the plugin.")
    public static final Property<Plugin> settings = newBeanProperty(Plugin.class, "settings", new Plugin().populate());

}