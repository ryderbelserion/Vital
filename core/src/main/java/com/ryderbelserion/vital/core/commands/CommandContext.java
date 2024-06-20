package com.ryderbelserion.vital.core.commands;

import com.ryderbelserion.vital.core.util.StringUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import java.util.ArrayList;
import java.util.List;

public class CommandContext {

    private final ArrayList<String> args;
    private final Audience audience;
    private String label;

    public CommandContext(final Audience audience, final String label, final List<String> args) {
        this.audience = audience;
        this.label = label;

        this.args = new ArrayList<>();

        this.args.addAll(args);
    }

    public void reply(final String message) {
        if (empty(message)) return;

        this.audience.sendMessage(StringUtil.parse(message));
    }

    public void reply(final Component message) {
        if (message == null) return;

        this.audience.sendMessage(message);
    }

    public void send(final Component component) {
        if (component == null) return;

        this.audience.sendMessage(component);
    }

    public void send(final Audience audience, final String message) {
        if (empty(message)) return;

        audience.sendMessage(StringUtil.parse(message));
    }

    public void send(final Audience audience, final Component component) {
        if (component == null) return;

        audience.sendMessage(component);
    }

    private boolean empty(String message) {
        return message.isBlank() || message.isEmpty();
    }

    public Audience getSender() {
        return this.audience;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public ArrayList<String> getArgs() {
        return this.args;
    }
}