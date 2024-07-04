package com.ryderbelserion.vital.paper.plugins.interfaces;

import java.util.UUID;

public abstract class Plugin {

    public abstract boolean isEnabled();

    public abstract String getName();

    public abstract void register();

    public abstract void unregister();

    public boolean isVanished() { return false; }

    public UUID getPlayer() { return UUID.randomUUID(); }

}