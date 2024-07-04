package com.ryderbelserion.vital.paper.plugins.interfaces;

public abstract class Plugin {

    public abstract boolean isEnabled();

    public abstract String getName();

    public boolean isVanished() { return false; }

}