package com.ryderbelserion.vital.paper.commands;

import com.ryderbelserion.vital.core.commands.args.Argument;
import com.ryderbelserion.vital.paper.VitalPaper;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PaperCommandManager {

    private final @NotNull VitalPaper paper = (VitalPaper) VitalPaper.api();

    private final @NotNull JavaPlugin plugin = this.paper.getPlugin();

    private final ConcurrentHashMap<String, PaperCommandEngine> commands = new ConcurrentHashMap<>();

    private final LinkedList<PaperCommandEngine> classes = new LinkedList<>();

    private String namespace;

    public void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    public void addCommand(final PaperCommandEngine command, final boolean initial) {
        add(command, initial);
    }

    public void removeCommand(final PaperCommandEngine command, final boolean initial) {
        if (!hasCommand(command.getName())) return;

        if (!command.getCommands(command).isEmpty()) {
            command.getCommands(command).forEach(other -> {
                List<Argument> optional = command.getOptionalArgs(other);
                List<Argument> required = command.getRequiredArgs(other);

                if (!optional.isEmpty()) optional.clear();

                if (!required.isEmpty()) required.clear();
            });

            command.removeCommand(command, initial);
        }

        if (initial) {
            Command map = this.plugin.getServer().getCommandMap().getCommand(command.getName());

            if (map != null) map.unregister(this.plugin.getServer().getCommandMap());

            this.commands.remove(command.getName());
        } else this.classes.remove(command);
    }

    public final boolean hasCommand(final String label) {
        return this.commands.containsKey(label);
    }

    private void add(final PaperCommandEngine command, final boolean initial) {
        if (hasCommand(command.getName())) return;

        if (!command.isVisible()) {
            if (hasCommand(command.getName())) removeCommand(command, initial);

            return;
        }

        if (initial) {
            this.commands.put(command.getName(), command);

            // Add it to the command map.
            this.plugin.getServer().getCommandMap().register(this.namespace, command);
        } else {
            if (!this.classes.contains(command)) this.classes.add(command);
        }
    }

    public final Map<String, PaperCommandEngine> getCommands() {
        return Collections.unmodifiableMap(this.commands);
    }

    public final LinkedList<PaperCommandEngine> getClasses() {
        return this.classes;
    }
}