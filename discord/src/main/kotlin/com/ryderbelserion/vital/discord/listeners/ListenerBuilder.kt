package com.ryderbelserion.vital.discord.listeners

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.hooks.ListenerAdapter

public class ListenerBuilder(private val jda: JDA) {

    /**
     * Registers a listener.
     */
    private fun register(listener: ListenerAdapter) {
        this.jda.addEventListener(listener)
    }

    /**
     * Registers multiple listeners.
     */
    public fun register(vararg listeners: ListenerAdapter) {
        listeners.forEach(::register)
    }
}