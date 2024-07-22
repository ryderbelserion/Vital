package com.ryderbelserion.vital.commands.types.admin;

import com.ryderbelserion.vital.commands.types.BaseCommand;
import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionType;

import java.util.List;

public class CommandItem extends BaseCommand {

    @Command("item")
    @Permission(value = "vital.item", def = PermissionDefault.OP)
    public void reload(Player player) {
        ItemBuilder builder = new ItemBuilder();

        builder.withType(Material.POTION).setPotionType(PotionType.HARMING).setColor(Color.GREEN);

        player.getInventory().addItem(builder.getStack());
    }
}