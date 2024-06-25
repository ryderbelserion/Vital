package com.ryderbelserion.vital.discord.util

import dev.minn.jda.ktx.util.SLF4J

public object LogUtil {
    private val logger by SLF4J("Beidou")

    public fun info(message: () -> String) {
        return logger.info(message())
    }

    public fun warn(message: () -> String) {
        return logger.warn(message())
    }

    public fun severe(message: () -> String) {
        return logger.error(message())
    }
}