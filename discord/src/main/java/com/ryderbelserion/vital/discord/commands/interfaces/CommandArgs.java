package com.ryderbelserion.vital.discord.commands.interfaces;

public interface CommandArgs {

    int getArgAsInt(final int index, final String message, final boolean notifySender);

    float getArgAsFloat(final int index, final String message, final boolean notifySender);

    boolean getArgAsBoolean(final int index, final String message, final boolean notifySender);

    long getArgAsLong(final int index, final String message, final boolean notifySender);

    double getArgAsDouble(final int index, final String message, final boolean notifySender);

}