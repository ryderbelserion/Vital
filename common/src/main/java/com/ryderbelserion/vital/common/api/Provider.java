package com.ryderbelserion.vital.common.api;

import com.ryderbelserion.vital.common.VitalAPI;

/**
* A provider class to handle the api.
*
* @version 2.7-SNAPSHOT
* @since 1.0
*/
public final class Provider {

    /**
     * A provider class to handle the api.
     * @since 0.0.1
     */
    private Provider() {
        throw new AssertionError();
    }

    static VitalAPI api;

    /**
     * Gets {@link VitalAPI}.
     *
     * @return {@link VitalAPI}
     * @since 0.0.1
     */
    public static VitalAPI getApi() {
        return api;
    }
}