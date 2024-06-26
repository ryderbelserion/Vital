package com.ryderbelserion.vital.discord.embed;

import com.ryderbelserion.vital.discord.util.ColorUtil;
import java.awt.*;

public enum EmbedColor {

    DEFAULT("#bff7fd"),
    SUCCESS("#0eeb6a"),
    FAIL("#e0240b"),
    WARNING("#eb6123"),
    EDIT("#5e68ff");

    private final String code;

    EmbedColor(final String code) {
        this.code = code;
    }

    public final Color getColor() {
        return ColorUtil.toColor(this.code);
    }
}