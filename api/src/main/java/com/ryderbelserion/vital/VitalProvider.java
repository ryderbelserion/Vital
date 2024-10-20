package com.ryderbelserion.vital;

import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.UnavailableException;
import org.jetbrains.annotations.NotNull;

public class VitalProvider {

    private static Vital vital = null;

    public static @NotNull Vital get() {
        Vital instance = VitalProvider.vital;

        if (instance == null) {
            throw new UnavailableException();
        }

        return instance;
    }

    public static void register(final Vital vital) {
        VitalProvider.vital = vital;
    }

    public static void unregister() {
        VitalProvider.vital = null;
    }
}