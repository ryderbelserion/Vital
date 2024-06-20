package com.ryderbelserion.vital.paper.commands.reqs;

import org.bukkit.permissions.Permission;

public class PaperRequirementsBuilder {

    private boolean isPlayer = false;
    private Permission permission = null;
    private String rawPermission = "";

    public PaperRequirementsBuilder isPlayer(final boolean isPlayer) {
        this.isPlayer = isPlayer;

        return this;
    }

    public final boolean isPlayer() {
        return this.isPlayer;
    }

    public PaperRequirementsBuilder withRawPermission(final String rawPermission) {
        this.rawPermission = rawPermission;

        return this;
    }

    public final String getRawPermission() {
        return this.rawPermission;
    }

    public final PaperRequirementsBuilder withPermission(final Permission permission) {
        this.permission = permission;

        return this;
    }

    public final Permission getPermission() {
        return this.permission;
    }

    public PaperRequirements build() {
        return new PaperRequirements(this.isPlayer, this.permission, this.rawPermission, this);
    }
}