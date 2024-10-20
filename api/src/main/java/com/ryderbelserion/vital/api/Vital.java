package com.ryderbelserion.vital.api;

public interface Vital {

    default String getNumberFormat() {
        return "#,###.##";
    }

    default boolean isVerbose() {
        return false;
    }

    default String getRounding() {
        return "half_even";
    }
}