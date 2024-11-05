package com.ryderbelserion.vital.common.api.managers.files.enums;

public enum FileType {

    YAML("yaml"),
    JSON("json");

    private final String prettyName;

    FileType(final String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return this.prettyName;
    }
}