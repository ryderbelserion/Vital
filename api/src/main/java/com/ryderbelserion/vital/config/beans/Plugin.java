package com.ryderbelserion.vital.config.beans;

import ch.jalu.configme.Comment;
import ch.jalu.configme.beanmapper.ExportName;

/**
 * Holds data related to the library config
 *
 * @author ryderbelserion
 * @version 0.0.4
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
    @ExportName("is_verbose")
    public boolean verbose;

    /**
     * Number format
     */
    @Comment("Controls the format, of the numerical data.")
    @ExportName("number_format")
    public String numberFormat;

    /**
     * Rounding format
     */
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
        this.verbose = false;
        this.numberFormat = "#,###.##";
        this.rounding = "half_even";

        return this;
    }

    /**
     * Sets true or false
     *
     * @param verbose true or false
     */
    public void setVerbose(final boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Sets number format
     *
     * @param numberFormat the format
     */
    public void setNumberFormat(final String numberFormat) {
        this.numberFormat = numberFormat;
    }

    /**
     * Sets rounding
     *
     * @param rounding the rounding format
     */
    public void setRounding(final String rounding) {
        this.rounding = rounding;
    }

    /**
     * True or false
     *
     * @return true or false
     */
    public boolean isVerbose() {
        return this.verbose;
    }

    /**
     * Gets the number format
     *
     * @return number format
     */
    public String getNumberFormat() {
        return this.numberFormat;
    }

    /**
     * Gets the rounding format
     *
     * @return rounding format
     */
    public String getRounding() {
        return this.rounding;
    }
}