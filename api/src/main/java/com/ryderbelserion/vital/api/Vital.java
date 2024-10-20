package com.ryderbelserion.vital.api;

import java.io.File;

public interface Vital {

    default String getNumberFormat() {
        return "#,###.##";
    }

    default String getRounding() {
        return "half_even";
    }

    default File getDataFolder() {
        return null;
    }

    default File getDirectory() {
        return null;
    }
}