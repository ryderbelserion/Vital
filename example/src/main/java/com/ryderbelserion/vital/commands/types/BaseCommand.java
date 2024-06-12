package com.ryderbelserion.vital.commands.types;

import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.paper.files.config.FileManager;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Command(value = "vital")
public abstract class BaseCommand {

    protected @NotNull final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    protected @NotNull final FileManager fileManager = this.plugin.getFileManager();
}