package com.ryderbelserion.vital.api.exceptions;

import com.ryderbelserion.vital.utils.Methods;
import java.util.List;

/**
 * Throws a descriptive exception
 *
 * @author ryderbelserion
 * @version 2.0.0
 * @since 2.0.0
 */
public final class UnavailableException extends IllegalStateException {

    private static final List<String> message = List.of(
            "The Vital API isn't loaded yet.",
            "",
            "A list of potential reasons:",
            " 1) The plugin using vital did not enable, or is not installed.",
            " 2) The plugin using Vital did not enable it correctly.",
            " 3) The plugin is trying to use the api before the api enables."
    );

    /**
     * Throws a descriptive exception
     *
     * @author ryderbelserion
     * @since 2.0.0
     */
    public UnavailableException() {
        super(Methods.toString(message));
    }
}