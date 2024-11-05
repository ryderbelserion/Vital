package com.ryderbelserion.vital.command.subs;

import ch.jalu.configme.SettingsManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.api.files.CustomFile;
import com.ryderbelserion.vital.api.files.enums.FileType;
import com.ryderbelserion.vital.api.files.types.YamlCustomFile;
import com.ryderbelserion.vital.config.ConfigManager;
import com.ryderbelserion.vital.config.keys.ConfigKeys;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
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
            final CustomFile<? extends CustomFile<?>> file = this.plugin.getFileManager().getFile(name, FileType.YAML);

            if (file != null) {
                final YamlCustomFile root = (YamlCustomFile) file.getInstance();

                if (root != null) {
                    player.sendRichMessage("<light_purple>Test Option: " + root.getBooleanValueWithDefault(false, "test-option"));
                }
            }
        }

        /*this.plugin.getFileManager().getFiles().forEach((fileName, customFile) -> {
            final CustomFile<? extends CustomFile<?>> file = this.plugin.getFileManager().getFile(name, FileType.YAML);

            if (file == null) return;

            final YamlCustomFile root = (YamlCustomFile) file.getInstance();

            player.sendRichMessage("Crate Type: " + root.getStringValueWithDefault("CSGO", "Crate", "CrateType"));
            player.sendRichMessage("Starting Keys: " + root.getIntValueWithDefault(0, "Crate", "StartingKeys"));
        });*/

        final SettingsManager config = ConfigManager.getConfig();

        final boolean isVerbose = config.getProperty(ConfigKeys.settings).verbose;

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
        final PluginManager server = this.plugin.getServer().getPluginManager();

        final Permission permission = server.getPermission(getPermission());

        if (permission == null) {
            server.addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}