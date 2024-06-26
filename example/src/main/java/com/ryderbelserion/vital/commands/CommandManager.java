package com.ryderbelserion.vital.commands;

import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.commands.types.admin.CommandItem;
import com.ryderbelserion.vital.commands.types.admin.CommandReload;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandManager {

    private final static @NotNull TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    private final static @NotNull BukkitCommandManager<CommandSender> commandManager = BukkitCommandManager.create(plugin);

    /**
     * Loads commands.
     */
    public static void load() {
        List.of(
                new CommandReload(),
                new CommandItem()
        ).forEach(commandManager::registerCommand);
    }

    public static @NotNull BukkitCommandManager<CommandSender> getCommandManager() {
        return commandManager;
    }
}