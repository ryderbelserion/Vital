package com.ryderbelserion.vital.discord.commands.interfaces;

/**
 * The command args
 *
 * @author ryderbelserion
 * @version 0.0.3
 * @since 0.0.1
 */
public interface CommandArgs {

    /**
     * Gets the int arg
     *
     * @param index the position
     * @param message the message
     * @param notifySender true or false
     * @return the int
     * @since 0.0.1
     */
    int getArgAsInt(final int index, final String message, final boolean notifySender);

    /**
     * Gets the float arg
     *
     * @param index the position
     * @param message the message
     * @param notifySender true or false
     * @return the float
     * @since 0.0.1
     */
    float getArgAsFloat(final int index, final String message, final boolean notifySender);

    /**
     * Gets the boolean arg
     *
     * @param index the position
     * @param message the message
     * @param notifySender true or false
     * @return true or false
     * @since 0.0.1
     */
    boolean getArgAsBoolean(final int index, final String message, final boolean notifySender);

    /**
     * Gets the long arg
     *
     * @param index the position
     * @param message the message
     * @param notifySender true or false
     * @return the long
     * @since 0.0.1
     */
    long getArgAsLong(final int index, final String message, final boolean notifySender);

    /**
     * Gets the double arg
     *
     * @param index the position
     * @param message the message
     * @param notifySender true or false
     * @return the double
     * @since 0.0.1
     */
    double getArgAsDouble(final int index, final String message, final boolean notifySender);

}