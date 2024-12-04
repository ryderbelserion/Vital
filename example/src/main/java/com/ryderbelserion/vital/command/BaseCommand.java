package com.ryderbelserion.vital.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.files.enums.FileType;
import com.ryderbelserion.vital.paper.api.commands.PaperCommand;
import com.ryderbelserion.vital.paper.api.commands.context.PaperCommandInfo;
import com.ryderbelserion.vital.paper.api.files.PaperFileManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The base command
 */
public class BaseCommand extends PaperCommand {

    /**
     * The base command
     */
    public BaseCommand() {}

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    private final PaperFileManager fileManager = this.plugin.getFileManager();

    @Override
    public void execute(PaperCommandInfo info) {
        this.fileManager.init().reloadFiles();

        this.plugin.getPaper().reload();

        info.getCommandSender().sendRichMessage("<red>[Vital] You have reloaded the plugin.");
    }

    @Override
    public @NotNull final String getPermission() {
        return "vital.access";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("vital")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
                    execute(new PaperCommandInfo(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final PaperCommand registerPermission() {
        final PluginManager server = this.plugin.getServer().getPluginManager();

        final Permission permission = server.getPermission(getPermission());

        if (permission == null) {
            server.addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}