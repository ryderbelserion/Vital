package com.ryderbelserion.vital.common.util;

import ch.jalu.configme.SettingsManager;
import com.ryderbelserion.vital.common.config.ConfigManager;
import com.ryderbelserion.vital.common.config.keys.ConfigKeys;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Utilities related to MATH!
 *
 * @author ryderbelserion
 * @version 0.0.4
 * @since 0.0.1
 */
public class MathUtil {

    /**
     * Utilities related to MATH!
     *
     * @author ryderbelserion
     * @since 0.0.1
     */
    public MathUtil() {
        throw new AssertionError();
    }

    private static final SettingsManager config = ConfigManager.getConfig();

    /**
     * Converts a double to a string with rounding and proper formatting.
     *
     * @param value the double to format
     * @return the string
     * @since 0.0.1
     */
    public static String format(final double value) {
        final DecimalFormat decimalFormat = new DecimalFormat(config.getProperty(ConfigKeys.settings).numberFormat);

        decimalFormat.setRoundingMode(mode());

        return decimalFormat.format(value);
    }

    /**
     * Gets the rounding mode from the config
     *
     * @return the rounding mode
     * @since 0.0.1
     */
    public static RoundingMode mode() {
        return RoundingMode.valueOf(config.getProperty(ConfigKeys.settings).rounding.toUpperCase());
    }
}