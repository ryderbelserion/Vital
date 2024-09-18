package com.ryderbelserion.vital.command.subs;

import ch.jalu.configme.SettingsManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.common.managers.config.ConfigManager;
import com.ryderbelserion.vital.common.managers.config.keys.ConfigKeys;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import com.ryderbelserion.vital.paper.api.files.CustomFile;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandFile extends PaperCommand {

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    @Override
    public void execute(PaperCommandInfo info) {
        final String name = info.getStringArgument("name");

        final @NotNull CommandSender player = info.getCommandSender();

        if (!name.isEmpty()) {
            final CustomFile file = this.plugin.getFileManager().getFile(name);

            if (file != null) {
                final YamlConfiguration root = file.getConfiguration();

                if (root != null) {
                    player.sendRichMessage("<light_purple>Test Option: " + root.getBoolean("test-option", false));
                }
            }
        }

        this.plugin.getFileManager().getCustomFiles().forEach((fileName, customFile) -> {
            final YamlConfiguration configuration = customFile.getConfiguration();

            if (configuration == null) return;

            player.sendRichMessage("Crate Type: " + configuration.getString("Crate.CrateType", "CSGO"));
            player.sendRichMessage("Starting Keys: " + configuration.getInt("Crate.StartingKeys", 0));
        });

        final SettingsManager config = ConfigManager.getConfig();

        final boolean isVerbose = config.getProperty(ConfigKeys.settings).is_verbose;

        player.sendRichMessage("<yellow>Verbose: " + isVerbose);
    }

    @Override
    public @NotNull final String getPermission() {
        return "vital.file";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        registerPermission();

        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("file").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, String> arg1 = argument("name", StringArgumentType.string()).suggests((ctx, builder) -> {
            this.plugin.getFileManager().getFiles().keySet().forEach(builder::suggest);

            return builder.buildFuture();
        }).executes(context -> {
            execute(new PaperCommandInfo(context));

            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });;

        return root.then(arg1).build();
    }

    @Override
    public @NotNull final PaperCommand registerPermission() {
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}