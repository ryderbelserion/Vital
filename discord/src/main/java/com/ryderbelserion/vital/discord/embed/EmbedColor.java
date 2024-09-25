package com.ryderbelserion.vital.discord.embed;

import com.ryderbelserion.vital.common.utils.ColorUtil;
import java.awt.Color;

/**
 * Colors enum
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public enum EmbedColor {

    /**
     * The default color
     */
    DEFAULT("#bff7fd"),
    /**
     * The success color
     */
    SUCCESS("#0eeb6a"),
    /**
     * The failed color
     */
    FAIL("#e0240b"),
    /**
     * The warning color
     */
    WARNING("#eb6123"),
    /**
     * The edit color
     */
    EDIT("#5e68ff");

    private final String code;

    /**
     * Builds the embed color
     *
     * @param code the code
     * @since 0.0.1
     */
    EmbedColor(final String code) {
        this.code = code;
    }

    /**
     * Gets the color
     *
     * @return {@link Color}
     * @since 0.0.1
     */
    public final Color getColor() {
        return ColorUtil.toColor(this.code);
    }
}