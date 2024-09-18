package com.ryderbelserion.vital.common.managers.config.beans;

import ch.jalu.configme.Comment;

/**
 * Holds data related to the library config
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class Plugin {

    /**
     * Holds data related to the library config
     *
     * @author ryderbelserion
     * @since 0.0.1
     */
    public Plugin() {}

    /**
     * Tells the plugin to log messages, if true.
     */
    @Comment({
            "This option defines if the plugin/library logs everything,",
            "Very useful if you have an issue with the plugin!"
    })
    public boolean is_verbose;

    @Comment("Controls the format, of the numerical data.")
    public String numberFormat;

    @Comment({
            "This controls the type of rounding for how the numerical data is rounded.",
            "",
            "Available types: up, down, ceiling, floor, half_up, half_down, half_even, unnecessary"
    })
    public String rounding;

    /**
     * Populate the default options.
     *
     * @return {@link Plugin}
     */
    public final Plugin populate() {
        this.is_verbose = false;
        this.numberFormat = "#,###.##";
        this.rounding = "half_even";

        return this;
    }
}