package com.ryderbelserion.vital.commands.types.admin;

import com.ryderbelserion.vital.commands.types.BaseCommand;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.permissions.PermissionDefault;

public class CommandItem extends BaseCommand {

    @Command("item")
    @Permission(value = "vital.item", def = PermissionDefault.OP)
    public void item(Player player) {
        ItemBuilder builder = new ItemBuilder();

        //builder.withType("leather_chestplate#1000");
        builder.withType(Material.DIAMOND).setMaxStackSize(64).setAmount(99);

        Inventory inventory = player.getInventory();

        inventory.setMaxStackSize(64);

        inventory.addItem(builder.getStack());
    }
}