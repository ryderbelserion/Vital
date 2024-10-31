package com.ryderbelserion.vital.command.subs;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Tag;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.tag.DamageTypeTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandItem extends PaperCommand {

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    @Override
    public void execute(PaperCommandInfo info) {
        if (!info.isPlayer()) return;

        final ItemBuilder builder = new ItemBuilder();

        builder.withType(ItemType.DIAMOND_SWORD).setFireResistant().addDamageTag(DamageTypeTags.IS_FALL);

        final ItemStack itemStack = builder.asItemStack();

        final ItemMeta itemMeta = itemStack.getItemMeta();

        final ComponentLogger logger = this.plugin.getComponentLogger();

        if (itemMeta.hasDamageResistant()) {
            final @Nullable Tag<DamageType> tag = itemMeta.getDamageResistant();

            if (tag != null) {
                logger.warn("Tag: {}", tag);

                tag.getValues().forEach(value -> logger.warn("Value: {}", value));
            }
        }

        info.getPlayer().getInventory().addItem(builder.asItemStack());
    }

    @Override
    public @NotNull final String getPermission() {
        return "vital.item";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("item")
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