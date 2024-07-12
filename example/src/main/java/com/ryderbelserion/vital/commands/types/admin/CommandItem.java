package com.ryderbelserion.vital.commands.types.admin;

import com.ryderbelserion.vital.commands.types.BaseCommand;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

public class CommandItem extends BaseCommand {

    @Command("item")
    @Permission(value = "vital.item", def = PermissionDefault.OP)
    public void reload(Player player) {
        ItemBuilder builder = new ItemBuilder();

        builder.withType("leather_chestplate#1000");

        player.getInventory().addItem(builder.getStack());
    }
}