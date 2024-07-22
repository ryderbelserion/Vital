package com.ryderbelserion.vital.commands.types.admin;

import com.ryderbelserion.vital.commands.types.BaseCommand;
import com.ryderbelserion.vital.enums.Files;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import java.util.UUID;

public class CommandReload extends BaseCommand {

    @Command("reload")
    @Permission(value = "vital.reload", def = PermissionDefault.OP)
    public void reload(CommandSender sender) {
        Files.data.getConfiguration().set("Data", UUID.randomUUID().toString());
        Files.data.save();

        Files.config.reload();

        this.fileManager.init();

        sender.sendMessage(String.valueOf(Files.config.getConfiguration().getBoolean("test-option")));
    }
}