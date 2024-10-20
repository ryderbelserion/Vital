package com.ryderbelserion.vital.api;

import com.ryderbelserion.vital.VitalProvider;
import java.io.File;

public interface Vital {

    default void start() {
        VitalProvider.register(this);
    }

    default void stop() {
        VitalProvider.unregister();
    }

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